# LifeFX

**Author:** Darin Beaudreau

**Platform:** Java 8, JavaFX

### Description
A graphical UI containing implementations of three variants of Conway's Game of Life.
I have always had an interest in cellular automata, and I had never implemented anything besides the
original Conway's Game of Life rules. I originally only intended to implement the rules for the "Wireworld"
variant, which allows cellular automata to simulate electronic circuits, but after browsing around I
found several other variants I like. I decided to make this project generalized so that I could add more
rule sets later if I wanted to. This was also my first project using JavaFX. I have previously tried to
get into JavaFX, but found the tutorials lacklustre, but it seems that has changed, so I gave it a go,
and now I don't think I'll ever go back to Swing.

### Features
 - Four different variants of Conway's Game of Life.
     - Wireworld, a rule set that uses cellular automata to simulate electronic circuits.
     - Cyclic Cellular Automaton, a rule set that uses cell colors as a state machine to form interesting patterns.
     - Rainbow Game of Life, a rule set that uses multiple colors for cells, and each new cell that is born takes on the average color of its neighbors.
     - Custom mode, where you can specify a custom ruleset by using two strings of digits, separated by a "/", to designate how many neighbors are required for a cell to survive, and how many neighbors are required for a cell to be born. For example, the string representing the rules for the original Conway's Game of Life would be "23/3".
 - You can resize the canvas, up to a maximum of 500x500 (requires a very high-end computer).
 - You can change the simulation speed to cause generations to go by faster or slower.
 - There is a color palette for the Wireworld and Rainbow Game modes so you can draw the starting cells yourself.
 
### TODO
 - Add a save/load feature so patterns can be saved and reloaded for later use.
 - Add "WalledCities" variant.
