JCC = javac

JFLAGS = -g

default: hashtagcounter.class MaxFibonacciHeap.class Node.class

hashtagcounter.class: hashtagcounter.java
	$(JCC) $(JFLAGS) hashtagcounter.java

MaxFibonacciHeap.class: MaxFibonacciHeap.java
	$(JCC) $(JFLAGS) MaxFibonacciHeap.java

Node.class: Node.java
	$(JCC) $(JFLAGS) Node.java
	
clean: 
	$(RM) *.class