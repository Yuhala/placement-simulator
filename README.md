# VM placement simulator
- This program does VM placement simulation with MS Azure VM traces.
- There are 3 placement algorithms:
1. `Contiguous placement`: Here the algo places a VM such that its memory is contiguous. If there is no available server which can offer contiguous memory any server with available resources is chosen.
2. `Biggest server`: Here the server with the most available resources amongst servers already on in use by other VMs is chosen, else a new one is "switched on" and used.
3. `Smallest server`: Here the server with the least available resources amongst servers already in use by other VMs is chosen, else another is "switched on" and used.

## Doing placement simulation
- Clone the repo to your local environment and build the project:
```
git clone https://github.com/Yuhala/placement-simulator.git
cd placement-simulator && make

```
- The traces used for simulation are in the file `vmtable.csv`.
- Run placement simulation with the provided script: 
```
./run.sh

```
- By default algo 1: contiguous placement is chosen. You can change this option in the `run.sh` file to use the other placement algorithms.
- At the end of simulation, the number of VMs, total number of servers (in data center), total number of servers used, and percentage of VMs with contiguous memory during their lifetimes is outputed.
- Other options like total number of servers can be modified in the `Main.java` file in `src/simulator` directory.

## Note
- This readme was written (on the fly) 2 years after the writing of the program, and may contain a few inconsistencies with respect to the code. 
- Please contact me in case of any issue or miscomprehension.