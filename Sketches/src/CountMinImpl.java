public class CountMinImpl {
    int width;
    int flowCount;
    int hashCount;
    int counter[][];
    int hashArray[];

    public CountMinImpl(int flowCount, int hashCount, int width) {
        this.flowCount = flowCount;
        this.hashCount = hashCount;
        this.width = width;
        this.hashArray = new int[hashCount];
        this.counter = new int[hashCount][width];

        //Initialize with zero
        for (int i = 0; i < hashCount; i++) {
            for (int j = 0; j < width; j++) {
                counter[i][j] = 0;
            }
        }
        this.hashArray = initializeHashArray(this.hashCount);
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
     * Return the array index to set the bit in bloom filter
     *
     * @param element    randomly generated element
     * @param index Number of hash functions to implement
     * @param tableSize Size of Hash Table
     * @return the arrayIndex to store the element into
     */
    public int hash(int element, int index, int tableSize) {
        int arrayIndex = -1;    //Initialize to minus 1
        int xID = element ^ this.hashArray[index];
        //further hashing
        xID = ((xID >> 16) ^ xID) * 0x45d9f3b;
        xID = ((xID >> 16) ^ xID) * 0x45d9f3b;
        xID = (xID >> 16) ^ xID;
        arrayIndex = xID % tableSize;
        return arrayIndex;
    }

    /**
     * 1. From the flowId, get the hashcode
     * 2. For i = 0 to k - 1, get the array index
     * 3. increment the array counter with the size of flow
     * @param flowId the array to be encoded into the bloom filter
     * @param flowSize the array to be encoded into the bloom filter
     */
    public void record (String flowId, int flowSize) {
        int flowID = flowId.hashCode();
        for (int i = 0; i < this.hashCount; i++) {
            int arrIndex = hash(flowID, i, this.width);
            //System.out.println("ID: " + flowId + " .... " + flowID + " .... " + arrIndex);
            this.counter[i][arrIndex] += flowSize;
        }
    }

    /**
     * Returns the min of the k values
     * @param flowId the flowId for which we want to query
     * @return min value of the k values
     */
   public int queryMin(String flowId) {
        int min = Integer.MAX_VALUE;
        int flowID = flowId.hashCode();
        for (int i = 0; i < this.hashCount; i++) {
           int arrIndex = hash(flowID, i, this.width);
           min = this.counter[i][arrIndex] < min ? this.counter[i][arrIndex] : min;
        }
        return min;
   }
}
