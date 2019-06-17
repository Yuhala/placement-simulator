
#
# Author: Peterson Yuhala
#

JFLAGS = -g -Xlint
JC = javac
SRC = ./src/simulator/*.java
CLASSES = ./src/classes
MAIN = simulator.Main

		  
default: $(SRC)
	$(JC) -sourcepath src -d $(CLASSES) $(SRC)


.PHONY: sim clean

clean:
	$(RM) $(CLASSES)/simulator/*.class


##Options:
# 1: contiguity aware placement
# 2: choose server with max free resources
# 3: choose server with min free resources

sim1: 
	java -cp $(CLASSES) simulator.Main 1

sim2: 
	java -cp $(CLASSES) simulator.Main 2

sim3: 
	java -cp $(CLASSES) simulator.Main 3