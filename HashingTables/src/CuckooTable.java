public class CuckooTable {
    int size;
    int hashCount;
    int cuckooSteps;
    int table[];
    int hashArray[];

    public CuckooTable(int size, int hashCount, int cuckooSteps) {
        this.size = size;
        this.hashCount = hashCount;
        this.cuckooSteps = cuckooSteps;
        table = new int[size];
        hashArray = new int[hashCount];
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
    public int[] initializeHashArray(int hashCount) {
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
     * @param index Number of hash functions to implement
     * @param tableSize Size of Hash Table
     * @return the arrayIndex to store the flow ID into
     */
    public int hash(int flowID, int index, int tableSize) {
        int arrayIndex = -1;    //Initialize to minus 1
        int xID = flowID ^ this.hashArray[index];
        //further hashing
        xID = ((xID >> 16) ^ xID) * 0x45d9f3b;
        xID = ((xID >> 16) ^ xID) * 0x45d9f3b;
        xID = (xID >> 16) ^ xID;
        arrayIndex = xID % tableSize;
//        System.out.println("FlowID: " + flowID + ", xID: " + xID + ", Arr Index: " + arrayIndex);
        return arrayIndex;
    }

    /**
     * 1. Generate Flow IDS randomly
     * 2. Assuming each flow is a packet, record one flow at a time
     * 3. Collision Handling - none. Ignore flows that cannot be stored in the table.
     *
     * @param flowSize  the number of flows into the hashtable
     */
    public void insert (int flowSize, int cuckooSteps) {
        this.hashArray = initializeHashArray(this.hashCount);
        int min = 1, max = Integer.MAX_VALUE;
        for (int i = 0; i < flowSize; i++) {
            int flowID = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
            insertionUtil(flowSize, flowID, cuckooSteps);
        }
    }

    public boolean insertionUtil(int flowSize, int flowID, int cuckooSteps) {
        //For up to k hash functions
        for (int j = 0; j < this.hashCount; j++) {
            int arrIndex = hash(flowID, j, this.size);
            //Can insert flow
            if (this.table[arrIndex] == 0) {
                this.table[arrIndex] = flowID;
                return true;
            } else {
                if (move(arrIndex, cuckooSteps)) {  //if a move is possible
                    this.table[arrIndex] = flowID;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean move(int arrIndex, int cuckooSteps) {
        if (cuckooSteps < 1) {
            return false;
        }

        int flowID = this.table[arrIndex];
        for (int i = 0; i < this.hashCount; i++) {
            int newTableIndex = hash(flowID, i, this.size);
            if (newTableIndex != arrIndex && this.table[newTableIndex] == 0) {
                this.table[newTableIndex] = flowID;
                return true;
            }
            move(newTableIndex, cuckooSteps - 1);
        }
        return false;
    }

    public void print () {
        int flowsRecorded = 0;
        for (int i = 0; i < this.size; i++) {
            if (this.table[i] != 0) {
                System.out.println("Index: " + i + ", Flow ID: " + this.table[i]);
                flowsRecorded++;
            }
        }
        System.out.println("\nNumber of flows in the table: " + flowsRecorded);
    }
}
