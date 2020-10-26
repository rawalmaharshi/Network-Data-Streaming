import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.Queue;

public class CountMin {
    public static void main(String[] args) {
        BufferedReader br;
        FileReader fr = null;
        String ipFile = args[0];
        FileWriter fw = null;
        String opFile = "count_min_sketch_output.txt";
        Queue<Que> topHundred = new PriorityQueue<>(100, (a,b) -> a.estimatedSize - b.estimatedSize);
        float average = 0;
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
                int estimatedSize = cm.queryMin(flowset[i]);
                int trueSize = flowsizeC[i];
                Que element = new Que(flowset[i], trueSize, estimatedSize);
                if (topHundred.size() < 100 || topHundred.peek().estimatedSize < estimatedSize) {
                    if (topHundred.size() == 100)
                        topHundred.remove();
                    topHundred.add(element);
                }
                int currDif = Math.abs(trueSize - estimatedSize);
                diffSum += currDif;
            }
            average = diffSum / flowCount;
            System.out.println("Average: " + average);
        } catch (Exception e) {
            System.out.println("Error reading from file: " + e);
        }

        //writing
        try {
            fw = new FileWriter(opFile);
            fw.write("Average Error among all flows: " + average + "\n\n");
            //Reverse the min priority queue (will get in reverse order so that max is on top)
            Que[] topElements = new Que[100];
            for (int i = 0; i < 100; i++) {
                topElements[i] = topHundred.poll();
            }

            for (int i = 99; i >= 0; i--) {
                Que element = topElements[i];
                fw.write("Flow ID: " + element.flowId + ", Estimated Size: " + element.estimatedSize
                        + ", True Size: " + element.trueSize + "\n");
            }
            fw.close();
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e);
        }
        System.out.println("Output in file: " + opFile);
    }
}