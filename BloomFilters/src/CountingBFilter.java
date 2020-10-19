import java.io.FileWriter;

public class CountingBFilter {
    public static void main(String[] args) {
        int noOfElements, noOfRemoveElements, noOfAddElements, noOfBits, noOfHashes;
        if (args.length == 5) {
            try {
                noOfElements = Integer.parseInt(args[0]);
                noOfRemoveElements = Integer.parseInt(args[1]);
                noOfAddElements = Integer.parseInt(args[2]);
                noOfBits = Integer.parseInt(args[3]);
                noOfHashes = Integer.parseInt(args[4]);
                System.out.println("Bloom Filter Size: " + noOfBits + ", No. of Elements: " + noOfElements + ", No.of Hash Functions: " + noOfHashes);

                CountingBFilterImpl cbf = new CountingBFilterImpl(noOfBits, noOfHashes);

                int [] setA = new int[noOfElements], setACopy = new int[noOfRemoveElements], setB = new int[noOfAddElements];
                int min = 1, max = Integer.MAX_VALUE;
                for (int i = 0; i < noOfElements; i++) {
                    int element = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                    setA[i] = element;
                }
                //Encodes elements in the bloom filter
                cbf.encode(setA);

                //Remove elements
                for (int i = 0; i < noOfRemoveElements; i++) {
                    setACopy[i] = setA[i]; //copies the elements from a
                }
                //remove call
                cbf.removeElem(setACopy);   //Removes the first 500(no of elements to remove) from set A

                //Add elements
                for (int i = 0; i < noOfAddElements; i++) {
                    int element = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                    setB[i] = element;
                }
                //add call
                cbf.encode(setB);

                try {
                    FileWriter fw = null;
                    String opFile = "counting_bloom_filter_output.txt";
                    fw = new FileWriter(opFile);
                    fw.write("After lookup of elements in A, No. of elements in the filter: " + cbf.lookup(setA));
                    fw.close();
                    System.out.println("Output in file: " + opFile);
                } catch (Exception e) {
                    System.err.println("Error printing to file. " + e);
                }

            } catch (Exception e) {
                System.err.println("Error: " + e);
                System.exit(1);
            }
        } else {
            System.err.println("Arguments mismatch. Expected 5 arguments.");
            System.exit(1);
        }
    }
}
