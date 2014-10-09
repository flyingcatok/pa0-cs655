JFLAGS = -g
JC = javac
JVM= java

.SUFFIXES: .java .class

.java.class:
	$(JC) $(JFLAGS) $*.java

CLASSES = \
	Constants.java \
	Packet.java \
	Flow.java \
	IncomingFlows.java \
	Event.java \
	Simulator.java \
	FIFORouter.java \
	RRRouter.java \
	DRRRouter.java \
	Experiment.java \

MAIN = Experiment 

default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
	$(RM) *.txt
