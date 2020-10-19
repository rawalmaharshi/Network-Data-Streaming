public class CountingBFilterImpl {
    int bitCount;
    int hashCount;
    int bitArray[];
    int hashArray[];

    public CountingBFilterImpl(int bitCount, int hashCount) {
        this.bitCount = bitCount;
        this.hashCount = hashCount;
        bitArray = new int[bitCount];
        hashArray = new int[hashCount];
        //Initialize with zero
        for (int i = 0; i < bitCount; i++) {
            bitArray[i] = 0;
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
     * 1. Encode the array into the filter i.e. generate index using each of hash function
     * 2. Set the index to 1 in the bitArray
     * @param setEncode the array to be encoded into the bloom filter
     */
    public void encode (int [] setEncode) {
        for (int i = 0; i < setEncode.length; i++) {
            int element = setEncode[i];
            //For up to k hash functions
            for (int j = 0; j < this.hashCount; j++) {
                int arrIndex = hash(element, j, this.bitCount);
                //increment the counter of 'mth' bit
                this.bitArray[arrIndex]++;
            }
        }
    }

    /**
     * @param removeSet the array to be removed from the bloom filter
     */
    public void removeElem (int [] removeSet) {
        for (int i = 0; i < removeSet.length; i++) {
            int element = removeSet[i];
            //For up to k hash functions
            for (int j = 0; j < this.hashCount; j++) {
                int arrIndex = hash(element, j, this.bitCount);
                //decrement the counter of 'mth' bit
                this.bitArray[arrIndex]--;
            }
        }
    }

    /**
     * @param lookupSet the array to perform lookup on in the bloom filter
     * @return the number of elements present in the bloom filter
     */
    public int lookup (int [] lookupSet) {
        int noOfElementsPresent = 0;
        for (int i = 0; i < lookupSet.length; i++) {
            int element = lookupSet[i];
            for (int j = 0; j < this.hashCount; j++) {
                int arrIndex = hash(element, j, this.bitCount);
                //element is not present
                if (this.bitArray[arrIndex] <= 0) {
                    break;
                }
                //reaches here only if all bits are set to (>= 1)
                if (j == this.hashCount - 1) {
                    noOfElementsPresent++;
                }
            }
        }
        return noOfElementsPresent;
    }

    public void print () {
        for (int i = 0; i < this.bitCount; i++) {
            System.out.println("[" + i + "] -> Count: " + this.bitArray[i]);
        }
    }
}
