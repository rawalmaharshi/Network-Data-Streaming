public class TableNode {
    int size;
    int hashCount;
    int flowsRecorded;
    int table[];

    public TableNode(int size, int hashCount) {
        this.size = size;
        this.hashCount = hashCount;
        this.flowsRecorded = 0;
        table = new int[size];
        //Initialize with zero
        for (int i = 0; i < size; i++) {
            table[i] = 0;
        }
    }

    /**
     * Function to initialize a hash array (s[])
     *
     * @param hashCount Number of hash functions to use
     * @return Randomly generated s[]
     */
    public static int[] initializeHashArray(int hashCount) {
        int[] randomNumArray = new int[hashCount];
        int min = 0, max = Integer.MAX_VALUE;
        for (int i = 0; i < hashCount; i++) {
            int random = (int) (Math.random() * (max - min)); //Returns value in the range 0 - Integer.MAX_VALUE
            randomNumArray[i] = random;
        }
        return randomNumArray;
    }

    /**
     * Implement the multiple hash function
     * Return the array index to store flow ID
     *
     * @param flowID    randomly generated flowID
     * @param hashCount Number of hash functions to implement
     * @param tableSize Size of Hash Table
     * @return the arrayIndex to store the flow ID into
     */
    public static int hash(int flowID, int hashCount, int tableSize) {
        int arrayIndex = -1;    //Initialize to minus 1
        int s[] = initializeHashArray(hashCount);
        for (int i = 0; i < s.length; i++) {
            flowID = flowID ^ s[i];
        }
        arrayIndex = flowID % tableSize;
        return arrayIndex;
    }

    /**
     * 1. Generate Flow IDS randomly
     * 2. Assuming each flow is a packet, record one flow at a time
     * 3. Collision Handling - none. Ignore flows that cannot be stored in the table.
     *
     * @param flowSize  the number of flows into the hashtable
     */
    public void insert (int flowSize) {
        int min = 1, max = Integer.MAX_VALUE;
        for (int i = 0; i < flowSize; i++) {
            int flowID = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
            int arrIndex = hash(flowID, this.hashCount, this.size);
//            System.out.println(flowID + " " + arrIndex);
            //Can insert flow
            if (this.table[arrIndex] == 0) {
                this.table[arrIndex] = flowID;
                this.flowsRecorded++;
            } else {
                //Ignore that flow
                continue;
            }
        }
    }

    public void print () {
        System.out.println("Number of flows in the table: " + this.flowsRecorded);
        for (int i = 0; i < this.size; i++) {
            if (this.table[i] != 0) {
                System.out.println("Index: " + i + ", Flow ID: " + this.table[i]);
            }
        }
    }
}
