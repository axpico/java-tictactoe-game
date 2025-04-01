# java-tictactoe-game

## Description

This project is a classic Tic-Tac-Toe game with a graphical user interface built using Java Swing. It features both a two-player mode and an AI opponent with multiple difficulty levels.

## Features

- Graphical User Interface using Java Swing
- Two-player mode
- AI opponent with three difficulty levels:
    - Easy: Makes random moves
    - Medium: Can block player wins and make winning moves
    - Hard: Uses minimax algorithm for optimal play
- Real-time game status display
- New Game button to restart at any time
- Difficulty selector to change game mode or AI level

## Project Structure

The project follows the Model-View-Controller (MVC) architectural pattern:

- `TicTacToe.java`: Main class to start the application
- `Model.java`: Handles game logic and state
- `View.java`: Manages the user interface
- `Controller.java`: Processes user input and updates the model and view

## How to Run

1. Ensure you have Java Development Kit (JDK) installed on your system.
2. Clone the repository:
   ```
   git clone https://github.com/axpico/java-tictactoe-game.git
   ```
3. Navigate to the project directory:
   ```
   cd java-tictactoe-game
   ```
4. Compile the Java files:
   ```
   javac *.java
   ```
5. Run the application:
   ```
   java TicTacToe
   ```

## How to Play

1. Launch the game.
2. Select the game mode (Two Players or AI difficulty) from the dropdown menu.
3. Click on an empty cell to make a move.
4. The game status will be displayed above the board.
5. To start a new game at any time, click the "New Game" button.

## Requirements

- Java Development Kit (JDK) 8 or higher

## License

This project is open source and available under the [MIT License](LICENSE).
