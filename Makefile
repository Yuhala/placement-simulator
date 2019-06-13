
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

sim: 
	java -cp $(CLASSES) simulator.Main