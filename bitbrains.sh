#!/bin/sh

dir=./*.jar


for f in $dir
do 
 java -jar $f
done
