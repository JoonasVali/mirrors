Mirrors (C) Joonas Vali  (2014-2016)

Simulation of an arbitrary physical system, where the goal is achieved using artificial evolution. 
The simulation can be used to demonstrate how random mutations manage to create an overall progress towards perfection.

You need:
- Java 8 installed. JAVA_HOME set and java runtime in the path.
- Maven (I used 3.0.5 and 2.2.1, both worked).


Getting started:
        **** YOU NEED TO BUILD THE APPLICATION AS A FIRST STEP ****
	Build application by maven command:

	'mvn clean install'

	It will be fully runnable under ./target/mirrors/mirrors (You can copy it anywhere from there once built.)
	The runnables are located at ./target/mirrors/mirrors/bin folder

Evolution

	use runner under 'evolution', to start evolution.
	
	- Windows: evolution/run-evolution.cmd
	- Unix: evolution/run-evolution.sh

	You might want to edit the values in evolution.properties at some point to tweak the parameters of evolution.
	Evolution is run on background, the resulting .pol files are stored by default in the mirrors root folder under 'saved'.
	Only the most successful variations from the generation are saved, rest are discarded. If generation does not provide any improvement
	the variations are not saved to avoid duplicates.
	
Show simulation

	Simulation can be shown using runner 'load' under './target/mirrors/mirrors/bin/single'. It allows you to choose one .pol file to run.
	- Windows: single/load.cmd
	- Unix: single/load.sh
	
Test random simulation
	
	Random simulation can be run using runner 'run-random' under './target/mirrors/mirrors/bin/single' to test the engine.
	- Windows: single/run-random.cmd
	- Unix: single/run-random.sh
