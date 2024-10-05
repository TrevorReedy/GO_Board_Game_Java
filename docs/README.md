# GO_Board_Game

A classic implementation of the board game GO, written in Java with Swing for a graphical interface. This version focuses on scoring territories and capturing opponent pieces according to the Japanese rules.

## **Features**

- 9x9 grid representation of the GO board (future update: dynamically change size of board)
- Accurate scoring of territories
- Basic rules of GO including capturing, liberties, and passing, 
- Graphical representation of the game using Java Swing

## **Getting Started**

### **Prerequisites**
- Java Development Kit (JDK) 8 or higher
- A Java IDE like IntelliJ IDEA, Eclipse, or VSCode (optional)

### **Installation**

**1. Clone the repository:**
```bash
git clone https://github.com/TrevorReedy/GO_Game_Java.git
```
**2.Navigate to file directory**
```bash
cd GO_Board_Game_Java
```
**3. Compile the java files**
```bash
javac src/main/*.java src/piece/*.java
```

**4. Run the game**
```bash
java -cp src/main Board
```

Gameplay Instructions
1) The game starts with the board empty. Black plays first, followed by white.
2) Click on an empty intersection to place a stone of the current turn's color.
3) A player may pass their turn by pressing the "Pass" button. Two consecutive passes end the game, and the scores are calculated.
4) Captured stones are automatically removed and added to the opponent's score.
5) The player with the highest score at the end wins. (This is currently displayed in console, but will be displayed on screen)


**File Structure**
<pre><code>
  GO-Game/
│
├── src/
│   ├── main/
│   │   ├── Board.java           # Core game board logic and state management
│   │   ├── Input.java           # Handles mouse input and user interactions
│   │   ├── pieces/
│   │   │   └── pieces.java      # Represents individual pieces on the board
│   │   └── move.java            # Handles move logic and properties
│   │
├── assets/
│   └── images/                  # (Optional) Any images or sprites used for UI
│
├── docs/
│   └── README.md                # Documentation and instructions for the game
│
├── lib/                         # Any external libraries (if needed)
│
├── build/                       # Compiled classes and build files

</code></pre>


**Contributing**
Contributing
Feel free to submit pull requests or report any issues. Contributions are welcome!

**License**
<pre><code>

This project is licensed under the MIT License - see the LICENSE file for details.</code></pre><code/>


