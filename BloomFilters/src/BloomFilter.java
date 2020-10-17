public class BloomFilter {
    public static void main(String[] args) {
        int noOfElements, noOfBits, noOfHashes;
        if (args.length == 3) {
            try {
                noOfElements = Integer.parseInt(args[0]);
                noOfBits = Integer.parseInt(args[1]);
                noOfHashes = Integer.parseInt(args[2]);
                System.out.println("Bloom Filter Size: " + noOfBits + ", No. of Elements: " + noOfElements + ", No.of Hash Functions: " + noOfHashes);

                int [] setA = new int[noOfElements], setB = new int [noOfElements];
                int min = 1, max = Integer.MAX_VALUE;
                for (int i = 0; i < noOfElements; i++) {
                    int element1 = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                    int element2 = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                    setA[i] = element1;
                    setB[i] = element2;
//                    System.out.println("A: " + setA[i] + ", B: " + setB[i]);
                }

                BloomFilterImpl filt = new BloomFilterImpl(noOfBits, noOfHashes);
                filt.encode(setA);
                System.out.println("After lookup of elements in A, No. of elements in the filter: " + filt.lookup(setA));
                System.out.println("After lookup of elements in B, No. of elements in the filter: " + filt.lookup(setB));
            } catch (Exception e) {
                System.err.println("Error parsing arguments. Expected an integer.");
                System.exit(1);
            }
        } else {
            System.err.println("Arguments mismatch. Expected 3 arguments.");
            System.exit(1);
        }
    }
}
