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

## LICENSE: ##

This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

In jurisdictions that recognize copyright laws, the author or authors
of this software dedicate any and all copyright interest in the
software to the public domain. We make this dedication for the benefit
of the public at large and to the detriment of our heirs and
successors. We intend this dedication to be an overt act of
relinquishment in perpetuity of all present and future rights to this
software under copyright law.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
OTHER DEALINGS IN THE SOFTWARE.

For more information, please refer to http://unlicense.org
