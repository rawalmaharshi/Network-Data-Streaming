public class DLeftHashing {
    public static void main(String[] args) {
        int noOfEntries, noOfFlows, noOfSegments;
        if (args.length == 3) {
            try {
                noOfEntries = Integer.parseInt(args[0]);
                noOfFlows = Integer.parseInt(args[1]);
                noOfSegments = Integer.parseInt(args[2]);
                System.out.println("Table Size: " + noOfEntries + ", No. of Flows: " + noOfFlows + ", No.of Segments: " + noOfSegments);
                DLeftTable tab = new DLeftTable(noOfEntries, noOfSegments);
                tab.insert(noOfFlows);
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
