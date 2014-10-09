CS655 Computer Networks PA 0
============================
Author: Feiyu Shi, Rakshit Sachdev
----------------------------------
Simulator Design
----------------
This project is built upon object-oriented design pattern. 
We have the following classes and interfaces to depict the whole system:
* Constants: a place to hold all the constants in the project.
* Packet: describes attributes and behaviors of a packet.
* Flow: a flow is a list of packets together with some useful information about the source.
* IncomingFlows: a list of flows fed into each router.
* Event: not only contains information of time and event name, but also the associated packet.
* Simulator: an interface describing the behaviors of a simulator for FIFO router, Round Robin (RR) router and a Deficit Round Robin (DRR) router.
* FIFORouter: a class implementing the simulator interface, and simulating FIFO router.
* RRRouter: a RR router.
* DRRRouter: a DRR router.
* Experiment: experiments are designed in here.

A simulator has at least these attributes: a schedule for holding and planning events, queue(s) to store waiting packets and a “server” dispatching packets. In a schedule, there are “birth” events, which means at some time a packet arrives in the system; there are “death” events, which means at a certain time a packet is started being served; there could be other events like ”logging” eventing to monitor system states.

A simulator also has the following behaviors: initializing schedule, doing something when a birth or death event happens and a controller to navigate the simulator according to the schedule.

When a birth event happens, we need to add the packet into the queue and schedule the next birth event. When a death event happens, we need to dequeue the packet and send it out of the system, and schedule the next death event. In the controller, each time it pop one event in the schedule and execute it and pop the next event. When there is no event in the schedule, the simulator is stopped.

Statistics Collection
---------------------
We measured the throughput and the average latency (queuing delay) of each flow. We collect these measurements for different total offered load M = 0.4 to 2.0. 

How to Run the Simulator
------------------------
* Compile: make
* Execute: java Experiment
* Clean: make clean
