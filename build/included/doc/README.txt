Mirrors (C) Joonas Vali  (2014-2019)

Simulation of an arbitrary physical system, where the goal is achieved using artificial evolution. 
The simulation can be used to demonstrate how random mutations manage to create an overall progress towards perfection.


## Setup evolution directory

As a first thing you need to create your evolution project by running the file:
   - Windows: `bin/create-evolution-project.bat`
   - Unix: `bin/create-evolution-project.sh`

and creating the evolution directory via the graphical user interface. This can be repeated later for more
evolution projects by creating another directory.

By default the file selector points you to the `projects` folder. You need to create a new directory there,
specifying the name. This `projects` folder is supposed to be a parent for all your separate evolution attempts.

## Evolution

Enter the created evolution directory and use the file 'evolve', to start evolution.

You might want to edit the values in evolution.properties at some point to tweak the parameters of evolution.
Evolution is run on background, the resulting models of evolution are stored in the `samples` directory as `json` files.
Only the most successful variations from the generation are saved, rest are discarded. If generation does not
provide any improvement the variations are not saved to avoid duplicates and conserve space.
	
## Launch simulation

The model can be shown using the `play` file in evolution directory. It allows you to choose a model
from the `samples` directory to see it in action. Press `space` to restart simulation.
You can launch this `play` process even during evolution to observe the progress in real time.

## Demo random simulation

When you change some of the properties in `evolution.properties` file you might want to see a random model
to observe the change result. For this you can use the `demo` file in the evolution directory.

## Pause and resume evolution

When you send the kill signal to the evolution process, it automatically tries to save it progress to `population.json`
file in evolution directory. Next time you run `evolve` it gets resumed from the last save point. On Windows and linux this
kill is done using `ctrl+c`

Notice that when force kill is done and process gets abruptly terminated the progress is lost.
