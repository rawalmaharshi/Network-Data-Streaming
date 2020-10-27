import java.io.FileWriter;
import java.util.Random;

public class ActiveCounter {
    public static void main(String[] args) {

        /**
         * Implementation using int counter
         */
        int zero = 0, one = 1;
        int cn = 0;
        int ce = 0;
        Random r = new Random();
        for (int i = 0; i < 1000000; i++) {
            if (cn < Math.pow(2, 16) - 1) {
                double randomValue = zero + (one - zero) * r.nextDouble();
                if (randomValue <= (1/Math.pow(2, ce))) {    //Prob of 1/2^ce
                    cn++;
                }
            } else {
                cn = cn >> 1;   //essentially divide by half
                ce++;
            }
        }

        /**
         * Implementation using short counter
         */
        Short cnn = Short.valueOf("0");
        Short cee = Short.valueOf("0");
        for (int i = 0; i < 1000000; i++) {
            if (cnn.intValue() < Math.pow(2, 15) - 1) {
                double randomValue = zero + (one - zero) * r.nextDouble();
                if (randomValue <= (1/Math.pow(2, cee))) {
                    cnn++;
                }
            } else {
                int temp = cnn.intValue();
                temp = temp/2;
                cnn = (short) temp;
                int temp1 = cee.intValue();
                temp1++;
                cee = (short) temp1;
            }
        }

        try {
            FileWriter fw = null;
            String opFile = "active_counter_output.txt";
            fw = new FileWriter(opFile);
            fw.write("Final Value of Active Counter: " + (int) (cn * Math.pow(2, ce)));
            fw.close();
            System.out.println("Output in file: " + opFile);
        } catch (Exception e) {
            System.err.println("Error printing to file. " + e);
        }

//        System.out.println("Final Value of Active Counter(Short): " + (int) (cnn.intValue() * Math.pow(2, cee.intValue())));
    }
}
