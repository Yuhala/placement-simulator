/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;

import java.util.ArrayList;


/**
 *
 * @author Peterson Yuhala
 */
public class VM {
    public static final double SLOT_SIZE = 100;
    public static int numVM = 0;
    public int id;
    public double totalMem;//Total vm memory in GB
    public double maxCPU;
    public int created;
    public int deleted;
    public int core_count;
    public Server hostingServer;
    public ArrayList<Integer> takenSlots = new ArrayList<Integer>();
    public boolean contiguousMem;
    public int vmslots;

    public VM(double mem, double cpu, int start, int stop, int cores) {
        this.id = numVM++;
        this.totalMem = mem;
        this.maxCPU = cpu;
        this.created = start;
        this.deleted = stop;
        this.core_count = cores;
        this.contiguousMem = false;
        this.vmslots = (int) Math.ceil((this.totalMem * 1000) / SLOT_SIZE);
    }

    public void chooseServer(ArrayList<Server> list) {
        boolean found = false;
        ArrayList<Server> serversOn = new ArrayList<Server>();
        ArrayList<Server> serversOff = new ArrayList<Server>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).hostingVMs.size() != 0) {
                serversOn.add(list.get(i));

            } else {
                serversOff.add(list.get(i));
            }

        }

        int start = 0;
        if (serversOn.size() > 0) {
            found = true;
            Server tempServer = serversOn.get(0);
            for (int i = 0; i < serversOn.size(); i++) {
                tempServer = serversOn.get(i);
                start = tempServer.findBlock(this);
                if (this.contiguousMem) {
                    break;

                }

            }
            this.hostingServer = tempServer;
           
        }
        if (!found && serversOff.size() > 0) {
            this.hostingServer = serversOff.get(0);           
            
        }
         
        // System.out.println(tempServer.id);
       if(this.hostingServer!=null){ this.hostingServer.addVM(this);}
       else{System.out.println("VM cannot be hosted: "+this.totalMem);return;}
    }

    public void chooseBiggest(ArrayList<Server> list) {
        int maxIndex = 0;
        double maxMem = 0;

        boolean found = false;
        ArrayList<Server> serversOn = new ArrayList<Server>();
        ArrayList<Server> serversOff = new ArrayList<Server>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).hostingVMs.size() != 0) {
                serversOn.add(list.get(i));

            } else {
                serversOff.add(list.get(i));
            }

        }

        if (serversOn.size() > 0) {
            found = true;
            for (int i = 0; i < serversOn.size(); i++) {

                if (serversOn.get(i).availableMem > maxMem) {
                    maxMem = serversOn.get(i).availableMem;
                    maxIndex = i;
                }

            }
            this.hostingServer = list.get(maxIndex);
        }

        if (!found && serversOff.size() > 0) {
            this.hostingServer = serversOff.get(0);            
        }

        this.hostingServer.addVM(this);
        
    }

    public void chooseSmallest(ArrayList<Server> list) {
        int minIndex = 0;
       

        boolean found = false;
        ArrayList<Server> serversOn = new ArrayList<Server>();
        ArrayList<Server> serversOff = new ArrayList<Server>();
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).hostingVMs.size() != 0) {
                serversOn.add(list.get(i));

            } else {
                serversOff.add(list.get(i));
            }

        }
       
        if (serversOn.size() > 0) {
            double minMem = serversOn.get(0).availableMem;
            found = true;
            for (int i = 0; i < serversOn.size(); i++) {

                if (serversOn.get(i).availableMem < minMem) {
                    minMem = serversOn.get(i).availableMem;
                    minIndex = i;
                }

            }
            this.hostingServer = list.get(minIndex);
        }
        if (!found && serversOff.size() > 0) {
            this.hostingServer = serversOff.get(0);
            
        }      

        this.hostingServer.addVM(this);
        
    }
}
