# Genetic Crossings
Ben Turner ([@Xeus](http://twitter.com/Xeus))
Nature of Code Final Project, Spring 2012, Prof. Daniel Shiffman

Project for my NYU ITP Introduction to Computational Media/Nature of Code classes.

## Description

Intended to create the building blocks for a system of people with randomly generated
characteristics who age, breed, pass along genetic information to offspring, and
interact with their environments, religious traits, and governments/cultures.

Coded in Processing.  Run "genetic_crossing_toxiclibs.pde" in Processing to view the sketch.  Also uses a node.js express server.

Repo includes back sketches as well as a Wolfram CA sketch and the node.js express JSON server which are also used for this sketch.

Most recent code uses these directories: `genetic_crossing_noc_final`, `WolframCA_test`, and `galapagus_json`.

## Credits

- Prof. Dan Shiffman for all his documentation and code from [Nature of Code](https://github.com/shiffman/The-Nature-of-Code), particularly his chapters on forces, genetic algorithms, ToxicLibs, and cellular automata
- Prof. John Schimmel for [his Processing-Nodejs code](https://github.com/johnschimmel/DWD-Processing)

## Installation

### Quick Way

Download these files and run them on your system; they're already built as applications.

MacOSX:
- 
- 

Windows:
- 
- 

### Native Way

Download [the latest version of Processing](http://processing.org/download/).

Clone my git repo. Open your Terminal and go to the directory parent you want to install to.  Then type `git clone git@github.com:Xeus/Genetic-Crossing.git`.

Navigate to the `./Genetic-Crossing` directory. Type `./galapagus_json` to change directories so you can do node.js stuff.

See `package.json` for dependencies.  Type `npm install` to install them. Uses
MongoDB and the Heroku toolbelt.

- http://www.mongodb.org/downloads
- https://toolbelt.heroku.com/

Set up your `.env` file to include your `MONGOLAB_URI` variable, which has your
user/pass to connect to MongoDB.

It should look like this:

	MONGOLAB_URI=mongodb://username:password@127.0.0.1:27017/json

Finally, type `foreman start` to start up the node.js express server.

From your file explorer or finder, open the `genetic_crossing_noc_final.pde` file in the `genetic_crossing_noc_final` directory so that it opens in Processing, where you can run it.  You'll also want to open `WolframCA_test.pde` from the `WolframCA_test` directory AFTER the node.js express server and the other sketch are running.

You'll end up having two Processing sketches running, plus a node.js express instance.

Then, experiment!  Right-click on the screen to move God.  Type `u` to pause physics, `space bar` to freeze the sketch completely, `f` to toggle funerals (hide dead people from the view).

## Links

- [Documentation blog](http://blog.benturner.com/2011/12/08/genetic-crossings-icm-final-project-presentation/)
- [Github](https://github.com/Xeus/Genetic-Crossing)

Also upped to OpenProcessing.org:
http://www.openprocessing.org/visuals/?visualID=47390

## Metadata

Last update 02 May 12: Demo'd in class and upped to Github.

Search for "TODO:" to find things requiring more work/fixes.