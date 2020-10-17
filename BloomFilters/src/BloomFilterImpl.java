import java.io.FileWriter;

public class BloomFilterImpl {
    int bitCount;
    int hashCount;
    int bitSet;
    int bitArray[];
    int hashArray[];

    public BloomFilterImpl(int bitCount, int hashCount) {
        this.bitCount = bitCount;
        this.hashCount = hashCount;
        this.bitSet = 0;
        bitArray = new int[bitCount];
        hashArray = new int[hashCount];
        //Initialize with zero
        for (int i = 0; i < bitCount; i++) {
            bitArray[i] = 0;
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
     * Return the array index to set the bit in bloom filter
     *
     * @param element    randomly generated element
     * @param index Number of hash functions to implement
     * @param tableSize Size of Hash Table
     * @return the arrayIndex to store the flow ID into
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
        this.hashArray = initializeHashArray(this.hashCount);
        for (int i = 0; i < setEncode.length; i++) {
            int element = setEncode[i];
            //For up to k hash functions
            for (int j = 0; j < this.hashCount; j++) {
                int arrIndex = hash(element, j, this.bitCount);  //To debug, replace flowID with i
                //set the bit to 1
                if (this.bitArray[arrIndex] != 1) {
                    this.bitArray[arrIndex] = 1;
                    this.bitSet++;
                }
            }
        }
    }

    public int lookup (int [] lookupSet) {
        int noOfElementsPresent = 0;
        for (int i = 0; i < lookupSet.length; i++) {
            int element = lookupSet[i];
            for (int j = 0; j < this.hashCount; j++) {
                int arrIndex = hash(element, j, this.bitCount);
                //element is not present
                if (this.bitArray[arrIndex] != 1) {
                    break;
                }
                //reaches here only if all bits are set to 1
                if (j == this.hashCount - 1) {
                    noOfElementsPresent++;
                }
            }
        }
        return noOfElementsPresent;
    }

    public void print () {
//        try {
//            FileWriter fw = null;
//            String opFile = "multi_hashing_output.txt";
//            fw = new FileWriter(opFile);
//            StringBuilder sb = new StringBuilder();
//            fw.write("Number of flows in the table: " + this.flowsRecorded + "\n");
//            for (int i = 0; i < this.size; i++) {
//                if (this.table[i] != 0) {
//                    sb.append("\n[" + i + "] -> Flow ID: " + this.table[i]);
//                } else {
//                    sb.append("\n[" + i + "] -> Entry: " + this.table[i]);
//                }
//            }
//            sb.append("\n");
//            fw.write(sb.toString());
//            fw.close();
//            System.out.println("Output in file: " + opFile);
//        } catch (Exception e) {
//            System.err.println("Error printing to file. " + e);
//        }
//        System.out.println("\n\nNumber of bits set in the table: " + this.bitSet);
        for (int i = 0; i < this.bitCount; i++) {
            if (this.bitArray[i] != 0) {
                System.out.println("Index: " + i + ", Flow ID: " + this.bitArray[i]);
            }
        }
    }
}
