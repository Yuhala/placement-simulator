/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulator;


import java.util.ArrayList;


/**
 *
 * @author peterson
 */
public class Server {

    public static final double SLOT_SIZE = 100;
    public static int numServers = 0;
    public int id;
    public double totalMem;//In GB
    public int totalCPU;//Number of CPUs
    public int[] memory;
    public ArrayList<VM> hostingVMs = new ArrayList<VM>();
    public int vmCount = 0;
    public int availableCPU;
    public double availableMem;//Available memory in GB
    public ArrayList<Integer> freeSlots = new ArrayList<Integer>();

    public Server(int mem, int cpu) {

        this.totalCPU = cpu;
        this.totalMem = mem * 0.1; // 1 slot=100MB
        this.id = numServers++;
        this.memory = new int[mem];
        this.availableCPU = cpu;
        this.availableMem = this.totalMem;
        for (int i = 0; i < mem; i++) {
            this.freeSlots.add(i);
        }

    }

    public void addVM(VM vm) {
        this.allocateMemory(vm);
        this.availableCPU -= vm.core_count;
        this.hostingVMs.add(vm);
        this.vmCount++;

    }

    public void removeVM(VM vm) {
        this.availableCPU += vm.core_count;
        this.availableMem += vm.totalMem;
        for (int i = 0; i < vm.takenSlots.size(); i++) {
            this.freeSlots.add(vm.takenSlots.get(i));

        }
        this.hostingVMs.remove(vm);

    }

    public void allocateMemory(VM vm) {
        //System.out.println("Freeslots " + this.freeSlots.size());
        int index = this.findBlock(vm);
        int count = 0;//Check for the start of a contiguous block if possible
        // System.out.println(vm.vmslots+" Block start index: "+index);
        int vmslots = (int) Math.ceil((vm.totalMem * 1000) / SLOT_SIZE);
       // System.out.println("Memory " + this.memory.length);

       // System.out.println(index + " " + this.freeSlots.size());
        if (vm.contiguousMem) {
            for (int i = index; i < this.memory.length; i++) {
                this.freeSlots.remove((Integer) i);//The memory slot index has been taken
                count++;
                vm.takenSlots.add(i);

                //   System.out.print("Count "+count+" ");
                if (count == vmslots) {

                    break;
                }
            }
        } else {

            for (int j = 0; j < this.freeSlots.size(); j++) {
                vm.takenSlots.add(this.freeSlots.get(j));
                count++; // System.out.println("Count "+count+" ");
                this.freeSlots.remove(Integer.valueOf(this.freeSlots.get(j)));
                if (count == vmslots) {
                    break;
                }

            }

        }

        this.availableMem -= vm.totalMem;
    }

    public int findBlock(VM vm) {
        int start = 0, count = 0, remainder = this.memory.length;

        int vmslots = vm.vmslots;
        if (this.hostingVMs.isEmpty()) {
          //System.out.println("Came here"+this.hostingVMs.size());
            vm.contiguousMem = true;
            return 0;

        }

        while (remainder >= vmslots) {

            for (int i = start; i < this.memory.length; i++) {
                if (this.freeSlots.contains(i)) {
                    count++;
                    if (count == vmslots) {

                        break;
                    }
                } else {

                    start = i + 1;
                    count = 0;
                    remainder = this.memory.length - start;
                    break;
                    
                }
            }
            if (count == vmslots) {
                break;
            }
        }

        if (count == vmslots) {
           // System.out.println("Count: " + count);
            vm.contiguousMem = true;
            // System.out.println(start);
            return start;

        } else {
            return 0;

        }

    }

}
