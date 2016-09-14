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
 - Three different variants of Conway's Game of Life.
     - Wireworld, a rule set that uses cellular automata to simulate electronic circuits.
     - Cyclic Cellular Automaton, a rule set that uses cell colors as a state machine to form interesting patterns.
     - Rainbow Game of Life, a rule set that uses multiple colors for cells, and each new cell that is born takes on the average color of its neighbors.
 - You can resize the canvas, up to a maximum of 500x500 (requires a very high-end computer).
 - You can change the simulation speed to cause generations to go by faster or slower.
 - There is a color palette for the Wireworld and Rainbow Game modes so you can draw the starting cells yourself.
 
### TODO
 - Add a save/load feature so patterns can be saved and reloaded for later use.
 - Modify Environment class to implement S/B method of defining game rules, whereas S = a set of numbers representing how many neighbors are required for a cell to survive, and B = a set of numbers representing how many neighbors are required for a cell to be born. (Ex: 23/3 [the origin Conway's Life rules])
 - Add "WalledCities" variant.
 - Add custom mode, where user can enter a their own rule set via the above modification to the Environment class to simulate their own custom rules.