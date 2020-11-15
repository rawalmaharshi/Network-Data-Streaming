import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;

public class MainFunction {
    public static void main(String[] args) {
        int flowCount;
        final int m, l;
        BufferedReader br;
        FileReader fr = null;
        String ipFile = args[0];
        FileWriter fw = null;
        String opFile = "virtual_bitmap_output.txt";
        //reading
        try {
            URL path = ClassLoader.getSystemResource(ipFile);
            fr = new FileReader(new File(path.toURI()));
            br = new BufferedReader(fr);
            String line = null;

            flowCount = Integer.parseInt(br.readLine());
            //m and l are constants for this demo
            m = 500000;
            l = 500;
            VirtualBitmap vb = new VirtualBitmap(flowCount, m, l);

            //A random number array
            int[] randomNumArray = new int[l];
            int min = 0, max = Integer.MAX_VALUE;
            for (int i = 0; i < l; i++) {
                int random = (int) (Math.random() * (max - min)); //Returns value in the range 0 - Integer.MAX_VALUE
                randomNumArray[i] = random;
            }

            String [] flowset = new String[flowCount];
            int [] flowsizeC = new int[flowCount];
            int [] estimatedSpreads = new int[flowCount];
            int flIndex = 0;

            while ((line = br.readLine()) != null) {
                String [] params = line.trim().replaceAll("[\\n\\t ]", " ").split(" ");
                String flowId = params[0], elementsInFlow = params[1];
                int elements = Integer.parseInt(elementsInFlow);
                flowset[flIndex] = flowId;
                flowsizeC[flIndex++] = elements;

                for (int i = 0; i < elements; i++) {
                    int random = (int) (Math.random() * (max - min));
                    int hashRandom = vb.hash(random);
                    int lIndex = vb.getArrIndex(hashRandom, l);

                    int flowID = flowId.hashCode();
                    int hashFlow = vb.hash(flowID);
                    int bIndex = vb.getArrIndex((randomNumArray[lIndex] ^ hashFlow), m);

                    //set the bIndex bit
                    vb.record(bIndex);
                }
            }

            double vbf = l * Math.log((double) (m - vb.bset.cardinality()) / m);
            //Get the estimate
            for (int i = 0; i < flowCount; i++) {
                int zeroCount = 0;
                int flowId = flowset[i].hashCode();
                flowId = vb.hash(flowId);
                for (int j = 0; j < l; j++) {
                    int bIndex = vb.getArrIndex((randomNumArray[j] ^ flowId), m);
                    if (!vb.query(bIndex)) {
                        zeroCount++;
                    }
                }

                double vf = l * Math.log((double) zeroCount / l);
                int estimatedSpread = (int) Math.round(vbf - vf);
                estimatedSpreads[i] = estimatedSpread;
            }

            //writing
            fw = new FileWriter(opFile);
            fw.write("Storing the true and estimated spreads for all flows:" + "\n\n");

            for (int i = 0; i < flowCount; i++) {
//                fw.write(flowsizeC[i] + ", " + estimatedSpreads[i] + "\n");
                fw.write("True Spread: " + flowsizeC[i] + ", Estimated Spread: " + estimatedSpreads[i] + "\n");
            }
            fw.close();

        } catch (Exception e) {
            System.out.println("Error in IO operations from file: " + e);
        }
    }
}