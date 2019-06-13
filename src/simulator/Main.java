/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

/**
 *
 * @author peterson
 */
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.Scanner;



//import Constants.NUM_SERVER;



public class Main {

    public static final double SLOT_SIZE = 100;//Memory slot size in MB  
    static final int NUM_SERVER = 20000;
    final double NUM_GEN2 = 0.1*NUM_SERVER;
    final double NUM_GEN3 = 0.2*NUM_SERVER;
    final double NUM_HPC = 0.2*NUM_SERVER;
    final double NUM_GEN4 = 0.2*NUM_SERVER;
    final double NUM_GEN5 = 0.1*NUM_SERVER;
    final double NUM_GOD = 0.1*NUM_SERVER;
    final double NUM_GEN6 = 0.1*NUM_SERVER;
    
    final int STEP = 300;
    final int SERVER_MEMORY_SIZE = 1280;//Number of slots ie 128GB
    final int NUM_CPU = 16;
    
    private static ArrayList<VM> vmList = new ArrayList();
    private static ArrayList<Event> eventList = new ArrayList();

    /*private static ArrayList<Server> hpc = new ArrayList<Server>();
    private static ArrayList<Server> gen2 = new ArrayList<Server>();
    private static ArrayList<Server> gen3 = new ArrayList();
    private static ArrayList<Server> gen4 = new ArrayList();
    private static ArrayList<Server> gen5 = new ArrayList();
    private static ArrayList<Server> godzilla = new ArrayList();
    private static ArrayList<Server> gen6 = new ArrayList();*/
    private static ArrayList<Server> svList = new ArrayList();
    private static ArrayList<Server> possibleServers = new ArrayList();
    //private static ArrayList<ArrayList<Server>> serverTypes = new ArrayList();
    private static int minTimestamp = 0;
    private static int maxTimestamp = 0;
    private static int contiguousCount = 0;
    private static int vmCount = 0;
    private static int serversUsed = 0;
    private static double percent = 0.0;
    static StringBuilder sb = new StringBuilder();
    static FileWriter pw;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {  
   

        pw = new FileWriter("test.csv", true);

        System.out.println("Starting simulation....");
        readCSV();
        Collections.sort(eventList);        
       
       // printEvents(eventList);
        
        vmCount = vmList.size();
        System.out.println("Number of VMs: " + vmCount);
        createServers();
        contiguousCount = 0;
        //ArrayList<Server> temp = serverTypes.get(i);
        simulate(svList);
        serversUsed = count(svList);
        percent = ((double) contiguousCount / (double) vmCount) * 100;
        writeResults(vmCount, NUM_SERVER, serversUsed, percent);

        pw.close();
        System.out.println("Ending Simulation.....");
        System.exit(0);

        /*for(int i=0;i<serverList.size();i++){
          Server temp=serverList.get(i);
           System.out.println("Server free slots: "+temp.freeSlots.size());
       }*/
    


    }
       

    public static void readCSV() throws FileNotFoundException {

        double mem, cpu;
        int start, stop, cores, max = 0, temp = 0;
        VM tempVm;

        try {
            Scanner vmScan = new Scanner(new File("vmtable.csv"));
            vmScan.useDelimiter(",");
            while (vmScan.hasNext()) {
                String[] inputArr = vmScan.nextLine().split(",");
                mem = Double.parseDouble(inputArr[10]);
                start = Integer.parseInt(inputArr[3]);
                stop = Integer.parseInt(inputArr[4]);
                cores = Integer.parseInt(inputArr[9]);

                temp = stop;
                
                max = (temp > max) ? temp : max;
                tempVm = new VM(mem, cores, start, stop, cores);
                vmList.add(tempVm);
                eventList.add(new Event(tempVm.id, 1, tempVm.created));
                eventList.add(new Event(tempVm.id, 0, tempVm.deleted));

                
                // System.out.println(mem + " " + cpu + " " + start + " " + stop + " " + cores);
            }
            maxTimestamp = max;
            vmScan.close();
            //serverScan.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    

    public static void createServers() {

        /*for (int i = 0; i < NUM_SERVER; i++) {
           // hpc.add(new Server(1280, 24));
            //gen2.add(new Server(320, 12));
            //gen3.add(new Server(1280, 16));
            //gen4.add(new Server(1920, 24));
            //gen5.add(new Server(2560, 40));
            //godzilla.add(new Server(5120,32));
           //gen6.add(new Server(1920, 48));
        }*/
        for (int i = 0; i < NUM_SERVER; i++) {
            svList.add(new Server(1920, 24));
        }
        /*for (int i = 0; i < NUM_GEN3; i++) {
            svList.add(new Server(1280, 16));
        }
        for (int i = 0; i < NUM_HPC; i++) {
            svList.add(new Server(1280, 24));
        }
        for (int i = 0; i < NUM_GEN4; i++) {
            svList.add(new Server(1920, 24));
       }
        for (int i = 0; i < NUM_GEN5; i++) {
            svList.add(new Server(2560, 40));
        }
        for (int i = 0; i < NUM_GEN6; i++) {
            svList.add(new Server(1920, 48));
        }
        for (int i = 0; i < NUM_GOD; i++) {
            svList.add(new Server(5120, 32));
        }*/

        //serverTypes.add(hpc);
        // serverTypes.add(gen2);
        //serverTypes.add(gen3);
        //serverTypes.add(gen4);
        // serverTypes.add(gen5);
        //serverTypes.add(godzilla);
        // serverTypes.add(gen6);
        /*  for (int i = 0; i < NUM_SERVER; i++) {
            System.out.println(serverList.get(i).id);
        }*/
    }

    public static void checkServers(VM vm, ArrayList<Server> serverList) {
        possibleServers.clear();
        for (int i = 0; i < serverList.size(); i++) {
            Server temp = serverList.get(i);
            if (temp.availableCPU >= vm.core_count && temp.availableMem >= vm.totalMem) {
                possibleServers.add(temp);
            }
        }

    }

    public static void simulate(ArrayList<Server> serverList) {
       
        VM tempVm=null;
        for (Event tempEvent: eventList) {           
            if (tempEvent.eventType == 0) {
              //VM destroyed
              tempVm = vmList.get(tempEvent.vmId); 
              tempVm.hostingServer.removeVM(tempVm);
              
               
            } else {//VM created
              tempVm = vmList.get(tempEvent.vmId);//Get VM corresponding to the event...O(1) 
                checkServers(tempVm, serverList);
               tempVm.chooseServer(possibleServers);
                //tempVm.chooseSmallest(possibleServers);
                //tempVm.chooseBiggest(possibleServers);
                if (tempVm.contiguousMem) {
                    contiguousCount++;
                }// serverTypes.add(gen5);  

            }

        }

        /*int size = vmList.size();
        int startTimestamp = vmList.get(0).created;
        VM tempVM;
        ArrayList<Integer> delIndices = new ArrayList<Integer>();
        for (int i = startTimestamp; i <= maxTimestamp; i++) {
            delIndices.clear();
            for (int j = 0; j < vmList.size(); j++) {
                tempVM = vmList.get(j);
                if (tempVM.created == i) {
                    //Host the given vm
                    checkServers(tempVM, serverList);
                    tempVM.chooseServer(possibleServers);
                    //tempVM.chooseSmallest(possibleServers);
                    //tempVM.chooseBiggest(possibleServers);
                    if (tempVM.contiguousMem) {
                        contiguousCount++;
                    }// serverTypes.add(gen5);

                }
                if (tempVM.deleted == i) {

                    //Remove the given vm from the system
                    tempVM.hostingServer.removeVM(tempVM);
                    delIndices.add(j);

                }
            }

            for (int k = 0; k < delIndices.size(); k++) {
                if (k < vmList.size()) {
                    vmList.remove(k);
                }
            }

        }*/

    }

    public static int count(ArrayList<Server> list) {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).vmCount != 0) {
                count++;
            }

        }
        return count;

    }

    public static void writeResults(int numVms, int numServers, int serversUsed, double percent) throws FileNotFoundException, IOException {

        sb.append(numVms);
        sb.append(',');
        sb.append(numServers);
        sb.append(',');
        sb.append(serversUsed);
        sb.append(',');
        sb.append(percent);
        sb.append('\n');
        pw.write(sb.toString());

    }
    public static void printEvents(ArrayList<Event>events){
       
    for(Event temp : events)     
        System.out.println(temp.vmId+" , "+temp.eventType+" , "+temp.timeStamp);
    }
    
}
