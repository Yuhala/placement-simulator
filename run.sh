#!/bin/bash

#Algo type numbers:
# 1: contiguity aware placement
# 2: use server with most resources (biggest server)
# 3: use server with least resources (smallest server)
ALGO=1
RES_FILE=sim-results.csv

cd src/classes

# remove old results if exist
rm $RES_FILE

java simulator.Main $ALGO

# print results
sed -i '1i Num. of VMs, Total Num. of servers, Servers Used, % contig' $RES_FILE
cat $RES_FILE