public class MyHashMap {

        //Ĭ�ϳ�ʼ����С 16
        private static final int DEFAULT_INITIAL_CAPACITY = 16;
        //Ĭ�ϸ������� 0.75
        private static final float DEFAULT_LOAD_FACTOR = 0.75f;

        //�ٽ�ֵ
        private int threshold;

        //Ԫ�ظ���
        private int size;

        //���ݴ���
        private int resize;

        private HashEntry[] table;

        public MyHashMap() {
            table = new HashEntry[DEFAULT_INITIAL_CAPACITY];
            threshold = (int) (DEFAULT_INITIAL_CAPACITY * DEFAULT_LOAD_FACTOR);
            size = 0;
        }

        private int findIndex(Object key) {
            //����key��hashcode��table����ȡģ����key��table�е�λ��
        	int index=key.hashCode() % table.length;
            return index >= 0 ? index : -index;
        }

        public void put(Object key) {
            //keyΪnullʱ��Ҫ���⴦��Ϊ��ʵ�ֺ���nullֵ
            //if (key == null) return;//return�������ǽ�������function
        	try {
            int index = findIndex(key);
            System.out.println(index);

            //����indexλ�õ�entry�����ҵ��ظ�key����¶�Ӧentry��ֵ��Ȼ�󷵻أ�������û���ҵ���entryΪnull������ѭ��
            HashEntry entry = table[index];
            while (entry != null) {
                if (entry.getKey().hashCode() == key.hashCode() && (entry.getKey() == key || entry.getKey().equals(key))) {
                    //entry.setValue(value);
                    return;
                }
                entry = entry.getNext();//ѭ������ÿһ��indexλ�õ�entry�����������ȣ�����add����
            }
            //��indexλ��û��entry����δ�ҵ��ظ���key������key��ӵ�table��indexλ��
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
            //���µ�entry�ŵ�table��indexλ�õ�һ������ԭ����ֵ����������ʽ���
            HashEntry entry = new HashEntry(key, table[index]);
            table[index] = entry;//entry��ֵ����table[index],table[index]ָ��entry
            //�ж�size�Ƿ�ﵽ�ٽ�ֵ�����Ѵﵽ��������ݣ���table��capacicy����
            if (size++ >= threshold) {
                resize(table.length * 2);
            }
        }

        private void resize(int capacity) {
            if (capacity <= table.length) {
            	return;
            }

            HashEntry[] newTable = new HashEntry[capacity];
            //����ԭtable����ÿ��entry�����¼���hash����newTable��
            for (int i = 0; i < table.length; i++) {
                HashEntry old = table[i];
                while (old != null) {
                    HashEntry temp = old.getNext();//old.next��obj
                    int index = findIndex(old.getKey());//��ΪfindIndex�ǻ������鳤�ȼ��㣬�������鳤�ȱ�Ϊԭ��������������Ҫ���¼���
                    old.setNext(newTable[index]);
                    newTable[index] = old;
                    old = temp;//ָ���滻
                }
            }
            //��newTable��table
            table = newTable;
            //�޸��ٽ�ֵ
            threshold = (int) (table.length * DEFAULT_LOAD_FACTOR);
            resize++;
        }

        public Object get(Object key) {
            //����򻯴�������nullֵ
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
        				//����ɹ��ҵ���ɾ�����޸�size
        				size--;
        				return entry.getKey();
        			}
        			pre = entry;//ָ���滻
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
        public String toString() {//��д��ԭ�е�toString����
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
    System.out.println("aSet = " + aMap );					//ע��������bMapһ��ֻput��bStrings����������Ԫ��


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
    
