public class MyHashMap {

        //默认初始化大小 16
        private static final int DEFAULT_INITIAL_CAPACITY = 16;
        //默认负载因子 0.75
        private static final float DEFAULT_LOAD_FACTOR = 0.75f;

        //临界值
        private int threshold;

        //元素个数
        private int size;

        //扩容次数
        private int resize;

        private HashEntry[] table;

        public MyHashMap() {
            table = new HashEntry[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            size = 0;
        }

        private int findIndex(Object key) {
            //根据key的hashcode和table长度取模计算key在table中的位置
        	int index=key.hashCode() % table.length;
            return index >= 0 ? index : -index;
        }

        public void put(Object key) {
            //key为null时需要特殊处理，为简化实现忽略null值
            //if (key == null) return;//return的作用是结束整个function
        	try {
            int index = findIndex(key);
            System.out.println(index);

            //遍历index位置的entry，若找到重复key则更新对应entry的值，然后返回，如果最后都没有找到，entry为null，跳出循环
            HashEntry entry = table[index];
            while (entry != null) {
                if (entry.getKey().hashCode() == key.hashCode() && (entry.getKey() == key || entry.getKey().equals(key))) {
                    //entry.setValue(value);
                    return;
                }
                entry = entry.getNext();//循环遍历每一个index位置的entry，如果都不相等，调用add方法
            }
            //若index位置没有entry或者未找到重复的key，则将新key添加到table的index位置
            add(index, key);
        	}catch(NullPointerException e){
        		System.out.println("NullPointerException caught");
        		this.put("null");
        	}
        }
        
        public void addAll(MyHashMap cMap) {   
        	
        	 for (HashEntry entry : cMap.table) {
                 while (entry != null) {
                     this.put(entry.getKey());
                     entry = entry.getNext();
                 }
             }
        }

        private void add(int index, Object key) {
            //将新的entry放到table的index位置第一个，若原来有值则以链表形式存放
            HashEntry entry = new HashEntry(key, table[index]);
            table[index] = entry;//entry的值赋给table[index],table[index]指向entry
            //判断size是否达到临界值，若已达到则进行扩容，将table的capacicy翻倍
            if (size++ >= threshold) {
                resize(table.length * 2);
            }
        }

        private void resize(int capacity) {
            if (capacity <= table.length) {
            	return;
            }

            HashEntry[] newTable = new HashEntry[capacity];
            //遍历原table，将每个entry都重新计算hash放入newTable中
            for (int i = 0; i < table.length; i++) {
                HashEntry old = table[i];
                while (old != null) {
                    HashEntry temp = old.getNext();//old.next是obj
                    int index = findIndex(old.getKey());//因为findIndex是基于数组长度计算，现在数组长度变为原来二倍，所以需要重新计算
                    old.setNext(newTable[index]);
                    newTable[index] = old;
                    old = temp;//指针替换
                }
            }
            //用newTable替table
            table = newTable;
            //修改临界值
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
            resize++;
        }

        public Object get(Object key) {
            //这里简化处理，忽略null值
            if (key == null) return null;
            HashEntry entry = getEntry(key);
            return entry == null ? null : entry.getKey();
        }

        public HashEntry getEntry(Object key) {
            HashEntry entry = table[findIndex(key)];
            while (entry != null) {
                if (entry.getKey().hashCode() == key.hashCode() && (entry.getKey() == key || entry.getKey().equals(key))) {
                    return entry;
                }
                entry = entry.getNext();
            }
            return null;
        }

        public Object remove(Object key) {
            //if (key == null) return null;
        	try {
        		int index = findIndex(key);
        		HashEntry pre = null;
        		HashEntry entry = table[index];
        		while (entry != null) {
        			if (entry.getKey().hashCode() == key.hashCode() && (entry.getKey() == key || entry.getKey().equals(key))) {
        				if (pre == null) table[index] = entry.getNext();
        				else pre.setNext(entry.getNext());
        				//如果成功找到并删除，修改size
        				size--;
        				return entry.getKey();
        			}
        			pre = entry;//指针替换
        			entry = entry.getNext();
        		}
        	}catch(NullPointerException e){
        		System.out.println("NullPointerException caught");
        		this.remove("null");
        	}
        	return null;
        }

        public boolean containsKey(Object key) {
            if (key == null) return false;
            return getEntry(key) != null;
        }

        public int size() {
            return this.size;
        }

        public void clear() {
            for (int i = 0; i < table.length; i++) {
                table[i] = null;
            }
            this.size = 0;
        }


        @Override
        public String toString() {//改写了原有的toString方法
            StringBuilder sb = new StringBuilder();
            sb.append(String.format("size:%s capacity:%s resize:%s\n\n", size, table.length, resize));
            for (HashEntry entry : table) {
                while (entry != null) {
                    sb.append(entry.getKey() + "\n");
                    entry = entry.getNext();
                }
            }
            return sb.toString();
        }
    

public static void main(String[] args) {
	// TODO Auto-generated method stub
	MyHashMap aMap = new MyHashMap();
	MyHashMap bMap = new MyHashMap();
    String[] aStrings = { "a", "b", "c" };
    String[] bStrings = { "A", "B", "C" };
    aMap.put(aStrings[0]); aMap.put(aStrings[1]);           // setup a, b
    bMap.put(bStrings[0]); bMap.put(bStrings[1]);           // setup A, B

    System.out.println("aSet = " + aMap );                  // --> a, b

    for (int index = 0; index < aStrings.length; index ++ ) {       // contans a and b, not c
            System.out.println("does " +
                            ( aMap.containsKey(aStrings[index]) ? "" : " not " ) + "contain: " +
                            aStrings[index] );
    }
    System.out.println("aSet = " + aMap );                  // --> a, b

    System.out.println("aSet.remove(aStrings[0]); = " + aMap.remove(aStrings[0]) ); // contains b
    System.out.println("aSet.remove(aStrings[2]); = " + aMap.remove(aStrings[2]) ); // can not remove x
    System.out.println("aSet = " + aMap );

    aMap.addAll(bMap);                                      // --> b, A, B
    System.out.println("aSet = " + aMap );					//注意在这里bMap一共只put了bStrings的其中两个元素


    aMap.put(null);                                         // --> b, A, B, null
    System.out.println("aSet = " + aMap );
    System.out.println("aSet.remove(null)= " + aMap.remove(null) );       // can remove null
}

    class HashEntry {
        private final Object key;
        //private Object value;
        private HashEntry next;
   
   

        public HashEntry(Object key,HashEntry next) {
            this.key = key;
            //this.value = value;
            this.next=next;
        }

        public Object getKey() {
            return key;
        }

//        public Object getValue() {
//            return value;
//        }

//        public void setValue(Object value) {
//            this.value = value;
//        }

        public HashEntry getNext() {
            return next;
        }

        public void setNext(HashEntry next) {
            this.next = next;
        }
    }
}
    
