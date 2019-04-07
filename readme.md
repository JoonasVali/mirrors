## Mirrors (C) Joonas Vali  (2014-2019) ##

"Mirrors" is a simulation of an arbitrary physical system, where the goal is achieved using artificial evolution. 
The simulation can be used to demonstrate how random mutations manage to create an overall progress towards perfection.

## Demonstration video: ##
https://www.youtube.com/watch?v=WEZ35350D2k

## Images ##

![Image 1](https://i.imgur.com/WCzh5E7.png)
![Image 2](https://i.imgur.com/LECMDJ5.png)


## What's going on there? ##

The simulation generates a wave of particles on a 2D plane and there exists a gathering point or goal for those particles, 
which counts how many and how fast the particles reached it by assigning score for each model (fitness of the model).
While the particle emitter and goal are constants on the model, there are many modifiers that can appear/disappear and change
in a particular model. The model composed of those modifiers is the evolving entity or organism, and
these modifiers in the model are generated via genes which are then either passed on during the selection process or not, based on how
well the model as a whole works. There are currently four types of modifiers: reflectors, benders, repellents and accelerators.

## Running mirrors ##

You need:
Java 8 installed. JAVA_HOME set and java runtime in the path.
Maven (I used 3.0.5 and 2.2.1, both worked).


### Building application from source ###

Build application by `mvn clean package`

It will be fully runnable under `.build/target/mirrors/mirrors` (You can copy it anywhere from there once built.)
The runnables are located at `./build/target/mirrors/mirrors/bin` folder