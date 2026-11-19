# 🐍 Snake Game – Java (Swing)

A modern, polished desktop implementation of the classic **Snake Game** built in Java using **Java Swing** and **AWT**. The game features smooth movement, responsive controls (Arrow keys and WASD), a pause/resume system, restart functionality, high-score tracking, and clean anti-aliased graphics.

---

## 🎮 Gameplay & Features

- **Responsive Controls**: Full support for both **Arrow Keys** and **WASD**.
- **Rapid Input Protection**: Prevents accidental self-collision when pressing two turn keys in quick succession (180° turn prevention).
- **Restart Without Relaunching**: Press <kbd>R</kbd>, <kbd>SPACE</kbd>, or <kbd>ENTER</kbd> on the Game Over screen to instantly start a new match.
- **Pause & Resume**: Press <kbd>P</kbd> or <kbd>SPACE</kbd> at any time to pause or resume your game.
- **Score & High Score Tracking**: Real-time HUD showing current score and the best score achieved in your session.
- **Smart Apple Spawning**: Apples are guaranteed to never spawn directly on top of the snake's body.
- **Polished Visuals**:
  - Smooth rendering with 2D anti-aliasing.
  - Distinct snake head with directional eyes.
  - Styled body segments with alternating colors.
  - Detailed apple graphic with stem, leaf, and light reflection.
  - Subtle grid overlay for precise movement judgment.
- **Thread Safety**: Initialized on the Swing Event Dispatch Thread (EDT) using `SwingUtilities.invokeLater`.

---

## 🕹️ Controls

| Action | Primary Key | Alternative Key |
| :--- | :--- | :--- |
| **Move Up** | <kbd>⬆ Up Arrow</kbd> | <kbd>W</kbd> |
| **Move Down** | <kbd>⬇ Down Arrow</kbd> | <kbd>S</kbd> |
| **Move Left** | <kbd>⬅ Left Arrow</kbd> | <kbd>A</kbd> |
| **Move Right** | <kbd>➡ Right Arrow</kbd> | <kbd>D</kbd> |
| **Pause / Resume** | <kbd>P</kbd> | <kbd>SPACE</kbd> |
| **Restart Game** *(on Game Over)* | <kbd>R</kbd> | <kbd>SPACE</kbd> / <kbd>ENTER</kbd> |

---

## 📂 Project Structure

```text
SnakeGame/
├── src/
│   ├── SnakeGame.java    # Application entry point (main method)
│   ├── GameFrame.java    # Top-level window (JFrame) configuration
│   └── GamePanel.java    # Game loop, rendering, input handling & physics
├── out/                  # Compiled class files (.class)
├── README.md             # Project documentation
└── SnakeGame-main.iml    # IntelliJ IDEA project module file
```

### Component Breakdown
- **`SnakeGame.java`**: Launches the GUI on the Swing Event Dispatch Thread via `SwingUtilities.invokeLater()`.
- **`GameFrame.java`**: Sets up the window title, default close operation, resizability, and centers the frame on screen.
- **`GamePanel.java`**: Contains all game logic, including:
  - Timer-driven game loop (`ActionListener`).
  - Coordinate tracking for snake segments.
  - Wall and self-collision detection.
  - Apple generation algorithm.
  - Keyboard listener (`KeyAdapter`).
  - Paint routines (`paintComponent` with `Graphics2D`).

---

## 📋 Prerequisites

- **Java Development Kit (JDK)**: Version 8 or newer (tested with JDK 17 & JDK 21).
  - Verify your installation:
    ```bash
    java -version
    javac -version
    ```

---

## 🚀 How to Run

### Option 1: Using the Command Line (Terminal / PowerShell / CMD)

1. **Clone the repository**:
   ```bash
   git clone https://github.com/YOUR_USERNAME/SnakeGame.git
   cd SnakeGame
   ```

2. **Compile the source files**:
   ```bash
   javac -d bin src/*.java
   ```

3. **Run the game**:
   ```bash
   java -cp bin SnakeGame
   ```

*(On Windows PowerShell, use backslashes `javac -d bin src\*.java` if required.)*

---

### Option 2: Running Directly with Java (Single-command)

If using Java 11+:
```bash
javac src/*.java
java -cp src SnakeGame
```

---

### Option 3: Using an IDE

#### **IntelliJ IDEA**
1. Open IntelliJ IDEA and select **Open** -> choose the project folder.
2. Ensure the Project SDK is configured (**File** > **Project Structure** > **Project** > select your installed JDK).
3. Navigate to `src/SnakeGame.java`.
4. Click the green **Run** button next to the `main` method (or press <kbd>Shift</kbd> + <kbd>F10</kbd>).

#### **Eclipse**
1. Select **File** > **Import** > **General** > **Projects from Folder or Archive**.
2. Point to the project folder and finish.
3. Right-click `SnakeGame.java` > **Run As** > **Java Application**.

#### **Visual Studio Code**
1. Install the **Extension Pack for Java**.
2. Open the project root in VS Code.
3. Open `src/SnakeGame.java` and click **Run** above `public static void main`.

---

## ⚙️ Game Rules & Mechanics

1. **Goal**: Eat as many red apples as possible. Each apple eaten increases your score by 1 and extends your snake by 1 segment.
2. **Speed**: The snake moves continuously at a fixed delay rate (80 ms tick).
3. **Collisions**:
   - Running into the walls (outer border) ends the game.
   - Colliding with your own body ends the game.
4. **Game Over**: When collision occurs, the final score and session high score are displayed alongside restart instructions.

---

## 💡 Future Enhancements

- [ ] Add sound effects for eating apples and game over.
- [ ] Add difficulty levels (Easy, Medium, Hard) or progressive speed scaling.
- [ ] Persistent high scores saved to a local file or preferences.
- [ ] Special bonus food (e.g., golden apples with timer countdowns).
- [ ] Customizable snake skins / color themes.

---

## 🧑‍💻 Author

- **Vishva K S**
- Contributions, issues, and feature suggestions are welcome!
