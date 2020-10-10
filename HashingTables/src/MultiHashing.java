public class MultiHashing {

    public static void main(String[] args) {
        int noOfEntries, noOfFlows, noOfHashes;
        if (args.length == 3) {
            try {
                noOfEntries = Integer.parseInt(args[0]);
                noOfFlows = Integer.parseInt(args[1]);
                noOfHashes = Integer.parseInt(args[2]);
                System.out.println("Table Size: " + noOfEntries + ", No. of Flows: " + noOfFlows + ", No.of Hash Functions: " + noOfHashes);

                int [] flowArray = new int[noOfFlows];
                int min = 1, max = Integer.MAX_VALUE;
                for (int i = 0; i < noOfFlows; i++) {
                    int flowID = (int) (Math.random() * (max - min)); //Returns value in the range 1 - Integer.MAX_VALUE
                    flowArray[i] = flowID;
                }

                MultiTable tab = new MultiTable(noOfEntries, noOfHashes);
                tab.insert(flowArray);
                tab.print();
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
