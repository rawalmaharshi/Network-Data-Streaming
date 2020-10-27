import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

public class MainFunction {
    public static void main(String[] args) {
        try {
            String sketchType = args[0];
            if (args.length == 1) {
                //active counter
                if (sketchType.equals("active")) {
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
//                    /**
//                     * Implementation using short counter
//                     */
//                    Short cnn = Short.valueOf("0");
//                    Short cee = Short.valueOf("0");
//                    for (int i = 0; i < 1000000; i++) {
//                        if (cnn.intValue() < Math.pow(2, 15) - 1) {
//                            double randomValue = zero + (one - zero) * r.nextDouble();
//                            if (randomValue <= (1/Math.pow(2, cee))) {
//                                cnn++;
//                            }
//                        } else {
//                            int temp = cnn.intValue();
//                            temp = temp/2;
//                            cnn = (short) temp;
//                            int temp1 = cee.intValue();
//                            temp1++;
//                            cee = (short) temp1;
//                        }
//                    }
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
                } else {
                    System.err.println("Arguments mismatch. Expected 'active' as 1st argument.");
                }
            } else if (args.length == 2) { //countmin or counter sketch
                if (sketchType.equals("countmin")) {
                    BufferedReader br;
                    FileReader fr = null;
                    String ipFile = args[1];
                    FileWriter fw = null;
                    String opFile = "count_min_sketch_output.txt";
                    Queue<Que> topHundred = new PriorityQueue<>(100, (a, b) -> a.estimatedSize - b.estimatedSize);
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
//                        System.out.println("Average: " + average);
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
                } else if (sketchType.equals("counter")) {
                    BufferedReader br;
                    FileReader fr = null;
                    String ipFile = args[1];
                    FileWriter fw = null;
                    String opFile = "counter_sketch_output.txt";
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
                        CounterSketchImpl cs = new CounterSketchImpl(flowCount, hashCount, width);
                        String [] flowset = new String[flowCount];
                        int [] flowsizeC = new int[flowCount];
                        int flIndex = 0;

                        while ((line = br.readLine()) != null) {
                            String [] params = line.trim().replaceAll(" +", " ").split(" ");
                            String flowId = params[0], packetSize = params[2];
                            //Record flows
                            cs.record(flowId, Integer.parseInt(packetSize));
                            flowset[flIndex] = flowId;
                            flowsizeC[flIndex++] = Integer.parseInt(packetSize);
                        }

                        int diffSum = 0;
                        for (int i = 0; i < flowCount; i++) {
                            int estimatedSize = cs.query(flowset[i]);
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
//                        System.out.println("Average: " + average);
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
                        System.out.println("Output in file: " + opFile);
                        fw.close();
                    } catch (Exception e) {
                        System.out.println("Error writing to file: " + e);
                    }
                } else {
                    System.err.println("Arguments mismatch. Expected 'countmin' or 'counter' as 1st argument.");
                }
            } else {
                System.err.println("Arguments mismatch. Please try again.");
                System.exit(1);
            }
        } catch (Exception e){
            System.err.println("Error: " + e);
        }
    }
}
