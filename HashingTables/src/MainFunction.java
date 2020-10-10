public class MainFunction {
    public static void main(String[] args) {
        String hashType = args[0];
        int noOfEntries, noOfFlows, noOfHashes, noOfCuckooSteps;

        if (args.length == 4) { //either of dleft or multi
            noOfEntries = Integer.parseInt(args[1]);
            noOfFlows = Integer.parseInt(args[2]);
            noOfHashes = Integer.parseInt(args[3]);

            int [] flowArray = new int[noOfFlows];
            int min = 1, max = Integer.MAX_VALUE;
            for (int i = 0; i < noOfFlows; i++) {
                int flowID = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                flowArray[i] = flowID;
            }

            if (hashType.equalsIgnoreCase("multi")) {
                MultiTable tab = new MultiTable(noOfEntries, noOfHashes);
                tab.insert(flowArray);
                tab.print();
            }

            else if (hashType.equalsIgnoreCase("dleft")) {
                DLeftTable tab = new DLeftTable(noOfEntries, noOfHashes);
                tab.insert(flowArray);
                tab.print();
            } else {
                System.err.println("Error parsing arguments. Expected either of 'multi' or 'dleft' as 1st argument.");
                System.exit(1);
            }
        } else if (args.length == 5) {
            noOfEntries = Integer.parseInt(args[1]);
            noOfFlows = Integer.parseInt(args[2]);
            noOfHashes = Integer.parseInt(args[3]);
            noOfCuckooSteps = Integer.parseInt(args[4]);

            //This array would be used for all hashing algorithms
            int [] flowArray = new int[noOfFlows];
            int min = 1, max = Integer.MAX_VALUE;
            for (int i = 0; i < noOfFlows; i++) {
                int flowID = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                flowArray[i] = flowID;
            }

            if (hashType.equalsIgnoreCase("cuckoo")) {
                CuckooTable tab = new CuckooTable(noOfEntries, noOfHashes, noOfCuckooSteps);
                tab.insert(flowArray, noOfCuckooSteps);
                tab.print();
            } else {
                System.err.println("Error parsing arguments. Expected 'cuckoo' as 1st argument.");
                System.exit(1);
            }
        } else if (args.length == 6) {
            noOfEntries = Integer.parseInt(args[1]);
            noOfFlows = Integer.parseInt(args[2]);
            noOfHashes = Integer.parseInt(args[3]);
            noOfCuckooSteps = Integer.parseInt(args[4]);
            int noOfSegments = Integer.parseInt(args[5]);

            //This array would be used for all hashing algorithms
            int [] flowArray = new int[noOfFlows];
            int min = 1, max = Integer.MAX_VALUE;
            for (int i = 0; i < noOfFlows; i++) {
                int flowID = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                flowArray[i] = flowID;
            }

            if (hashType.equalsIgnoreCase("all")) {  //implement all hashing algorithms
                MultiTable mlt = new MultiTable(noOfEntries, noOfHashes);
                mlt.insert(flowArray);
                mlt.print();

                DLeftTable dlft = new DLeftTable(noOfEntries, noOfSegments);
                dlft.insert(flowArray);
                dlft.print();

                CuckooTable cko = new CuckooTable(noOfEntries, noOfHashes, noOfCuckooSteps);
                cko.insert(flowArray, noOfCuckooSteps);
                cko.print();
            } else {
                System.err.println("Error parsing arguments. Expected 'all' as 1st argument.");
                System.exit(1);
            }
        } else {
            System.err.println("Arguments mismatch. Please try again.");
            System.exit(1);
        }
    }
}
