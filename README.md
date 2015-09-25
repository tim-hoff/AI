# AI

 Assignment 1: Missionary & Cannibals

## Installation - Prerequisites 

Instructions on running the program 
###Install homebrew (package manager for OS X)
	ruby -e "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/master/install)”

[Brew Website](http://brew.sh)

###Install leiningen (project manager for clojure) 
	brew install leiningen

###Go to the project folder
	$ cd ai
	$ lein deps
	$ lein run

To run the program interactively -
	$ lein repl

## Usage

A compiled standalone jar file is also included in the folder if you don’t want to bother w/ the compilation. It’s called ai.jar

    $ java -jar ai-0.1.0-standalone.jar [args]

## Options

...

## Examples

...

### Bugs

...

### The final states that we got for the solved problem

(33true 31false 32true 30false 31true 11false 22true 02false 03true 01false 11true 00false)

**Start state**	-  3, 3, boat 

**Target state** -  0, 0

| Left Side   | Right Side|
| ----------- | ----------|
| 3, 3, boat  |0, 0       |
| 3, 1		  |0, 2, boat |
| 3, 2, boat  |0, 1       |
| 3, 0		  |0, 3, boat |
| 3, 1, boat  |0, 2       |
| 1, 1		  |2, 2, boat |
| 2, 2, boat  |1, 1       |
| 0, 2		  |3, 1, boat |
| 0, 3, boat  |3, 0       |
| 0, 1		  |3, 2, boat |
| 1, 1, boat  |2, 2       |
| 0, 0		  |3, 3, boat |

### Notes
...

## License

Copyright © 2015 

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
