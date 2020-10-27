import java.util.Arrays;

public class CounterSketchImpl {
    int width;
    int flowCount;
    int hashCount;
    int counter[][];
    int hashArray[];

    public CounterSketchImpl(int flowCount, int hashCount, int width) {
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
     * @param element   randomly generated element
     * @param index     Number of hash functions to implement
     * @param tableSize Size of Hash Table
     * @return the arrayIndex to store the element into
     */
    public HashValues hash(int element, int index, int tableSize) {
        int arrayIndex = -1;    //Initialize to minus 1
        int xID = element ^ this.hashArray[index];
        //further hashing
        xID = ((xID >> 16) ^ xID) * 0x45d9f3b;
        xID = ((xID >> 16) ^ xID) * 0x45d9f3b;
        xID = (xID >> 16) ^ xID;
        //do the bit operations here
        int bitVal = getMSBit(xID);
        arrayIndex = xID % tableSize;
        HashValues hv = new HashValues(bitVal, Math.abs(arrayIndex));
        return hv;
    }

    /**
     * 1. From the flowId, get the hashcode
     * 2. For i = 0 to k - 1, get the array index
     * 3. increment the array counter with the size of flow
     *
     * @param flowId   the array to be encoded into the bloom filter
     * @param flowSize the array to be encoded into the bloom filter
     */
    public void record(String flowId, int flowSize) {
        int flowID = flowId.hashCode();
        for (int i = 0; i < this.hashCount; i++) {
            HashValues val = hash(flowID, i, this.width);
            int arrIndex = val.arrIndex;
            int bitSet = val.bitSet;
            //System.out.println("ID: " + flowId + " .... " + flowID + " .... " + arrIndex);
//            System.out.print("Before: " + this.counter[i][arrIndex]);
            this.counter[i][arrIndex] = (bitSet == 1 ) ? this.counter[i][arrIndex] + flowSize : this.counter[i][arrIndex] - flowSize;
//            System.out.print("\tMSB Bit: " + bitSet + "\tPacket Size: " + flowSize + "\tAfter: " + this.counter[i][arrIndex] + "\n");
        }
    }

    /**
     * Returns the median of the k values
     *
     * @param flowId the flowId for which we want to query
     * @return  median value
     */
    public int query(String flowId) {
        int flowID = flowId.hashCode();
        int [] flowVals = new int[this.hashCount];
        for (int i = 0; i < this.hashCount; i++) {
            HashValues val = hash(flowID, i, this.width);
            int arrIndex = val.arrIndex;
            int bitSet = val.bitSet;
            flowVals[i] = (bitSet == 1 ) ? this.counter[i][arrIndex] : -1 * this.counter[i][arrIndex];
        }
        Arrays.sort(flowVals);

        //return median value
        int median = this.hashCount % 2 == 1 ? flowVals[this.hashCount / 2] : (flowVals[(this.hashCount / 2) - 1] + flowVals[this.hashCount / 2]) / 2;
//        System.out.println("median: " + median);
        return median;
    }

    /**
     *
     * @param n the number whose MSB is needed
     * @return the MSB - 0 or 1
     */
    int getMSBit(int n) {
        //get the 31st bit
        boolean value = (n & (1 << 30)) != 0;
//        System.out.print(n + "..." + String.format("%32s", Integer.toBinaryString(n)).replace(' ', '0'));
//        System.out.print("\t 1st bit: " + value + "\n");
        return value == true ? 1 : 0;
    }
}

class HashValues {
    int bitSet;
    int arrIndex;

    public HashValues(int bitSet, int arrIndex) {
        this.bitSet = bitSet;
        this.arrIndex = arrIndex;
    }
}
