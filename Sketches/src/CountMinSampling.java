import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.*;

public class CountMinSampling {
    public static void main(String[] args) {
        BufferedReader br;
        FileReader fr = null;
        String ipFile = args[0];
        FileWriter fw = null;
        String opFile = "count_min_sketch_sampling_output.txt";
        Queue<Que> topHundred = new PriorityQueue<>(100, (a,b) -> a.estimatedSize - b.estimatedSize);
        double average = 0.0;
        Map<String, Integer> flowToSizeMap = new HashMap<>();
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
//            double probability = 01;
            while ((line = br.readLine()) != null) {
                String [] params = line.trim().replaceAll(" +", " ").split(" ");
                String flowId = params[0], packetSize = params[2];
                flowToSizeMap.put(flowId, Integer.parseInt(packetSize));
            }

            Random r = new Random();
            for (double p = 0.02; p <= 1.00; p += 0.02) {
                //record and then read
                CountMinSamplingImpl cm = new CountMinSamplingImpl(flowCount, hashCount, width);
                double diffSum = 0.0;
                average = 0.0;
                //Record flows
                //Here add a logic to get the randomized function
                //get the nextDouble
                Set<String> kSet = flowToSizeMap.keySet();
                for (String flowID : kSet) {
                    //record here
                    int sampleCount = 0;
                    int flowSize = flowToSizeMap.get(flowID);
                    for (int i = 0; i < flowSize; i++) {
                        Double d = r.nextDouble();
                        if (d < p) {
                            sampleCount++;
                        }
                    }
                    cm.record(flowID, sampleCount);
                }

                //query here
                for (String flowID : kSet) {
                    int estimatedSize = cm.queryMin(flowID) * (int)(1 / p);
                    int trueSize = flowToSizeMap.get(flowID);
                    Que element = new Que(flowID, trueSize, estimatedSize);
                    if (topHundred.size() < 100 || topHundred.peek().estimatedSize < estimatedSize) {
                        if (topHundred.size() == 100) {
                            topHundred.remove();
                        }
                        topHundred.add(element);
                    }
                    double currDif = Math.abs((double) (trueSize - estimatedSize));
                    diffSum += currDif;
                }
                average = diffSum / flowCount;
                System.out.println("For prob: " + p + ", average error: " + average);
                }
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
            System.out.println("Output in file: " + opFile);
        } catch (Exception e) {
            System.out.println("Error writing to file: " + e);
        }
    }
}