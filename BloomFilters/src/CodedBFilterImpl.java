import java.io.FileWriter;

public class CodedBFilterImpl {
    int filterCount;
    int bitCount;
    int hashCount;
    int filters[][];
    int hashArray[];
    String codesets[];

    public CodedBFilterImpl(int filterCount, int bitCount, int hashCount) {
        this.filterCount = filterCount;
        this.bitCount = bitCount;
        this.hashCount = hashCount;
        filters = new int[filterCount][bitCount];
        hashArray = new int[hashCount];
        //Initialize with zero
        for (int i = 0; i < filterCount; i++) {
            for (int j = 0; j < bitCount; j++) {
                filters[i][j] = 0;
            }
        }
        this.hashArray = initializeHashArray(this.hashCount);
        this.codesets = new String[] {"001", "010", "011", "100", "101", "110", "111"};
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
     * @param setEncode the set to be encoded into the bloom filter
     */
    public void encode (int [][] setEncode) {
        for (int i = 0; i < setEncode.length; i++) {
            for (int j = 0; j < setEncode[i].length; j++) {
                int element = setEncode[i][j];
                String enc = this.codesets[i];
                char[] cArr = enc.toCharArray();
                for (int c = 0; c < cArr.length; c++) {
                    //if cArr[c] is set to 1, then set the values only in that bloom filter
                    if (cArr[c] == '1') {
                        //For up to k hash functions
                        for (int k = 0; k < this.hashCount; k++) {
                            int arrIndex = hash(element, k, this.bitCount);
                            //set the value to 1 at index in the cth filter
                            this.filters[c][arrIndex] = 1;
                        }
                    }
                }
            }
        }
    }

    /**
     * @param lookupSet the array to perform lookup on in the bloom filter
     * @return the number of elements present in the bloom filter
     */
    public int lookup (int [][] lookupSet) {
        int noOfCorrectLookups = 0;
        for (int i = 0; i < lookupSet.length; i++) {
            for (int j = 0; j < lookupSet[i].length; j++) {
                int element = lookupSet[i][j];
                String actual = "";
                String expected = this.codesets[i];
                for (int k = 0; k < this.filterCount; k++) {
                    for (int l = 0; l < this.hashCount; l++) {
                        int arrIndex = hash(element, l, this.bitCount);
                        if (this.filters[k][arrIndex] != 1) {
                            actual += 0;
                            break;
                        }
                        //reaches here only if all bits are set to (>= 1)
                        if (l == this.hashCount - 1) {
                            actual += "1";
                        }
                    }

                    //if found
                    if (actual.equals(expected)) {
                        noOfCorrectLookups++;
                    }
                }
            }
        }
        return noOfCorrectLookups;
    }
//
//    public void print () {
////        try {
////            FileWriter fw = null;
////            String opFile = "multi_hashing_output.txt";
////            fw = new FileWriter(opFile);
////            StringBuilder sb = new StringBuilder();
////            fw.write("Number of flows in the table: " + this.flowsRecorded + "\n");
////            for (int i = 0; i < this.size; i++) {
////                if (this.table[i] != 0) {
////                    sb.append("\n[" + i + "] -> Flow ID: " + this.table[i]);
////                } else {
////                    sb.append("\n[" + i + "] -> Entry: " + this.table[i]);
////                }
////            }
////            sb.append("\n");
////            fw.write(sb.toString());
////            fw.close();
////            System.out.println("Output in file: " + opFile);
////        } catch (Exception e) {
////            System.err.println("Error printing to file. " + e);
////        }
//        for (int i = 0; i < this.bitCount; i++) {
//            System.out.println("[" + i + "] -> Count: " + this.bitArray[i]);
//        }
//    }
}
