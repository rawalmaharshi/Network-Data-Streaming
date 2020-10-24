import org.w3c.dom.ls.LSOutput;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.Arrays;

public class CountMin {
    public static void main(String[] args) {
        BufferedReader br;
        FileReader fr = null;
        String ipFile = args[0];
        FileWriter fw = null;
        String opFile = "count_min_sketch_output.txt";

        //reading
        try {
            URL path = ClassLoader.getSystemResource(ipFile);
            fr = new FileReader(new File(path.toURI()));
            br = new BufferedReader(fr);
            String line = null;
            int flowCount = Integer.parseInt(br.readLine());

            //Initialize a CountMinImpl object
            int hashCount = 3;
            int width = 3000;
            CountMinImpl cm = new CountMinImpl(flowCount, hashCount, width);
            String [] flowset = new String[flowCount];
            int [] flowsizeC = new int[flowCount];
            int flIndex = 0;

            while ((line = br.readLine()) != null) {
                String [] params = line.trim().replaceAll(" +", " ").split(" ");
                String flowId = params[0], packetSize = params[2];
//                System.out.println("Flow ID: " + flowId + ", Packet Size: " + packetSize);
                //Record flows
                cm.record(flowId, Integer.parseInt(packetSize));
                flowset[flIndex] = flowId;
                flowsizeC[flIndex++] = Integer.parseInt(packetSize);
            }

            int diffSum = 0;
            for (int i = 0; i < flowCount; i++) {
                int currDif = Math.abs(flowsizeC[i] - cm.queryMin(flowset[i]));
                diffSum += currDif;
                System.out.println("Diff: " + currDif);
            }
            System.out.println("Average: " + diffSum / flowCount);
        } catch (Exception e) {
            System.out.println("Error reading from file: " + e);
        }

        //writing
        try {
            fw = new FileWriter(opFile);
            fw.close();
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e);
        }
        System.out.println("Output in file: " + opFile);
    }
}
