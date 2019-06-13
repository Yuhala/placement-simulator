#!/bin/sh

dir=./*.jar

echo "--------Starting Simulations using max-------:$(date) " >> ./log.txt

for i in $(seq 1000 1000 10000)
do 
  head -n i vmtable.csv > vmtablemini.csv 
  for f in $dir
  do 
    java -jar $f
  done
done

echo "--------Ending Simulations using max-------:$(date) " >> ./log.txt



