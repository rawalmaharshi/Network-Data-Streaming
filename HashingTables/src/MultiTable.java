import java.io.FileWriter;
import java.io.IOException;

public class MultiTable {
    int size;
    int hashCount;
    int flowsRecorded;
    int table[];
    int hashArray[];

    public MultiTable(int size, int hashCount) {
        this.size = size;
        this.hashCount = hashCount;
        this.flowsRecorded = 0;
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
     * @param flows the array of flows into the hashtable
     */
    public void insert (int [] flows) {
        this.hashArray = initializeHashArray(this.hashCount);
        for (int i = 0; i < flows.length; i++) {
            int flowID = flows[i];
            //For up to k hash functions
            for (int j = 0; j < this.hashCount; j++) {
                int arrIndex = hash(flowID, j, this.size);  //To debug, replace flowID with i
                //Can insert flow
                if (this.table[arrIndex] == 0) {
                    this.table[arrIndex] = flowID;
                    this.flowsRecorded++;
                    break;
                } else {
                    //Ignore that flow
                    continue;
                }
            }
        }
    }

    public void print () {
        try {
            FileWriter fw = null;
            String opFile = "multi_hashing_output.txt";
            fw = new FileWriter(opFile);
            StringBuilder sb = new StringBuilder();
            fw.write("Number of flows in the table: " + this.flowsRecorded + "\n");
            for (int i = 0; i < this.size; i++) {
                if (this.table[i] != 0) {
                    sb.append("\n[" + i + "] -> Flow ID: " + this.table[i]);
                } else {
                    sb.append("\n[" + i + "] -> Entry: " + this.table[i]);
                }
            }
            sb.append("\n");
            fw.write(sb.toString());
            fw.close();
            System.out.println("Output in file: " + opFile);
        } catch (Exception e) {
            System.err.println("Error printing to file. " + e);
        }
//        System.out.println("\n\nNumber of flows in the table: " + this.flowsRecorded);
//        for (int i = 0; i < this.size; i++) {
//            if (this.table[i] != 0) {
//                System.out.println("Index: " + i + ", Flow ID: " + this.table[i]);
//            } else {
//                System.out.println("Index: " + i + ", Entry: " + this.table[i]);
//            }
//        }
    }
}
