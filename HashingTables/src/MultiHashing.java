public class MultiHashing {

    public static void main(String[] args) {
        int noOfEntries, noOfFlows, noOfHashes;
        if (args.length == 3) {
            try {
                noOfEntries = Integer.parseInt(args[0]);
                noOfFlows = Integer.parseInt(args[1]);
                noOfHashes = Integer.parseInt(args[2]);
                System.out.println("Table Size: " + noOfEntries + ", No. of Flows: " + noOfFlows + ", No.of Hash Functions: " + noOfHashes);
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
