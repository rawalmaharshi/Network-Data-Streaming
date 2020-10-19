import java.io.FileWriter;

public class CodedBFilter {
    public static void main(String[] args) {
        int noOfSets, noOfElements, noOfFilters, noOfBits, noOfHashes;
        if (args.length == 5) {
            try {
                noOfSets = Integer.parseInt(args[0]);
                noOfElements = Integer.parseInt(args[1]);
                noOfFilters = Integer.parseInt(args[2]);
                noOfBits = Integer.parseInt(args[3]);
                noOfHashes = Integer.parseInt(args[4]);

                CodedBFilterImpl cbf = new CodedBFilterImpl(noOfSets, noOfFilters, noOfBits, noOfHashes);

                int [][] set = new int[noOfSets][noOfElements];
                int min = 1, max = Integer.MAX_VALUE;
                for (int i = 0; i < noOfSets; i++) {
                    for (int j = 0; j < noOfElements; j++) {
                        int element = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                        set[i][j] = element;
                    }
                }
                cbf.encode(set);

                try {
                    FileWriter fw = null;
                    String opFile = "coded_bloom_filter_output.txt";
                    fw = new FileWriter(opFile);
                    fw.write("Number of elements whose lookup results are correct: " + cbf.lookup(set));
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
