public class MainFunction {
    public static void main(String[] args) {
        String filterType = args[0];
        int noOfElements, noOfBits, noOfHashes;

        if (args.length == 4) { //bloom filter
            noOfElements = Integer.parseInt(args[1]);
            noOfBits = Integer.parseInt(args[2]);
            noOfHashes = Integer.parseInt(args[3]);

            int [] flowArray = new int[noOfBits];
            int min = 1, max = Integer.MAX_VALUE;
            for (int i = 0; i < noOfBits; i++) {
                int flowID = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                flowArray[i] = flowID;
            }

//            if (filterType.equalsIgnoreCase("multi")) {
//                MultiTable tab = new MultiTable(noOfElements, noOfHashes);
//                tab.insert(flowArray);
//                tab.print();
//            }
//
//            else if (filterType.equalsIgnoreCase("dleft")) {
//                DLeftTable tab = new DLeftTable(noOfElements, noOfHashes);
//                tab.insert(flowArray);
//                tab.print();
//            } else {
//                System.err.println("Error parsing arguments. Expected either of 'multi' or 'dleft' as 1st argument.");
//                System.exit(1);
//            }
//        } else if (args.length == 5) {
//            noOfElements = Integer.parseInt(args[1]);
//            noOfBits = Integer.parseInt(args[2]);
//            noOfHashes = Integer.parseInt(args[3]);
//            noOfCuckooSteps = Integer.parseInt(args[4]);
//
//            //This array would be used for all hashing algorithms
//            int [] flowArray = new int[noOfBits];
//            int min = 1, max = Integer.MAX_VALUE;
//            for (int i = 0; i < noOfBits; i++) {
//                int flowID = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
//                flowArray[i] = flowID;
//            }
//
//            if (filterType.equalsIgnoreCase("cuckoo")) {
//                CuckooTable tab = new CuckooTable(noOfElements, noOfHashes, noOfCuckooSteps);
//                tab.insert(flowArray, noOfCuckooSteps);
//                tab.print();
//            } else {
//                System.err.println("Error parsing arguments. Expected 'cuckoo' as 1st argument.");
//                System.exit(1);
//            }
//        } else if (args.length == 6) {
//            noOfElements = Integer.parseInt(args[1]);
//            noOfBits = Integer.parseInt(args[2]);
//            noOfHashes = Integer.parseInt(args[3]);
//            noOfCuckooSteps = Integer.parseInt(args[4]);
//            int noOfSegments = Integer.parseInt(args[5]);
//
//            //This array would be used for all hashing algorithms
//            int [] flowArray = new int[noOfBits];
//            int min = 1, max = Integer.MAX_VALUE;
//            for (int i = 0; i < noOfBits; i++) {
//                int flowID = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
//                flowArray[i] = flowID;
//            }
//
//            if (filterType.equalsIgnoreCase("all")) {  //implement all hashing algorithms
//                MultiTable mlt = new MultiTable(noOfElements, noOfHashes);
//                mlt.insert(flowArray);
//                mlt.print();
//
//                DLeftTable dlft = new DLeftTable(noOfElements, noOfSegments);
//                dlft.insert(flowArray);
//                dlft.print();
//
//                CuckooTable cko = new CuckooTable(noOfElements, noOfHashes, noOfCuckooSteps);
//                cko.insert(flowArray, noOfCuckooSteps);
//                cko.print();
//            } else {
//                System.err.println("Error parsing arguments. Expected 'all' as 1st argument.");
//                System.exit(1);
//            }
//        } else {
//            System.err.println("Arguments mismatch. Please try again.");
//            System.exit(1);
        }
    }
}
