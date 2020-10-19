import java.io.FileWriter;

public class MainFunction {
    public static void main(String[] args) {
        String filterType = args[0];
        int noOfElements, noOfBits, noOfHashes, noOfRemoveElements, noOfAddElements, noOfSets, noOfFilters;
        if (args.length == 4) { //bloom filter
            try {
                noOfElements = Integer.parseInt(args[1]);
                noOfBits = Integer.parseInt(args[2]);
                noOfHashes = Integer.parseInt(args[3]);

                int [] setA = new int[noOfElements], setB = new int [noOfElements];
                int min = 1, max = Integer.MAX_VALUE;
                for (int i = 0; i < noOfElements; i++) {
                    int element1 = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                    int element2 = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                    setA[i] = element1;
                    setB[i] = element2;
                }

                BloomFilterImpl filt = new BloomFilterImpl(noOfBits, noOfHashes);
                filt.encode(setA);

                try {
                    FileWriter fw = null;
                    String opFile = "bloom_filter_output.txt";
                    fw = new FileWriter(opFile);
                    fw.write("After lookup of elements in A, No. of elements in the filter: " + filt.lookup(setA) + "\n");
                    fw.write("After lookup of elements in B, No. of elements in the filter: " + filt.lookup(setB));
                    fw.close();
                    System.out.println("Output in file: " + opFile);
                } catch (Exception e) {
                    System.err.println("Error printing to file. " + e);
                }

            } catch (Exception e) {
                System.err.println("Error" + e);
                System.exit(1);
            }
        } else if (args.length == 6) {
            if (filterType.equalsIgnoreCase("counting")) {
                try {
                    noOfElements = Integer.parseInt(args[1]);
                    noOfRemoveElements = Integer.parseInt(args[2]);
                    noOfAddElements = Integer.parseInt(args[3]);
                    noOfBits = Integer.parseInt(args[4]);
                    noOfHashes = Integer.parseInt(args[5]);

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
            } else if (filterType.equalsIgnoreCase("coded")) {
                try {
                    noOfSets = Integer.parseInt(args[1]);
                    noOfElements = Integer.parseInt(args[2]);
                    noOfFilters = Integer.parseInt(args[3]);
                    noOfBits = Integer.parseInt(args[4]);
                    noOfHashes = Integer.parseInt(args[5]);

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
                System.err.println("Error parsing arguments. Expected either of 'counting' or 'coded' as 1st argument.");
                System.exit(1);
            }
        } else {
            System.err.println("Arguments mismatch. Please try again.");
            System.exit(1);
        }
    }
}
