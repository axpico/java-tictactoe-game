import javax.swing.*;
import java.awt.*;

/**
 * The View class represents the graphical user interface (GUI) of the Tic-Tac-Toe game.
 * It interacts with the user and communicates user actions to the controller.
 */
public class View {
    private JFrame frame;
    private JButton[][] buttons;
    private JButton resetButton;
    private JLabel statusLabel;
    private JComboBox<String> difficultyComboBox;
    private Controller controller;

    /**
     * Constructs a View object and initializes the game board buttons.
     */
    public View() {
        buttons = new JButton[3][3];
    }

    /**
     * Sets the controller to handle user actions.
     *
     * @param controller the controller to handle user interactions
     */
    public void setActionListener(Controller controller) {
        this.controller = controller;
    }

    /**
     * Initializes the GUI components, including the game board, control panel, and status label.
     */
    public void initialize() {
        frame = new JFrame("Tic-Tac-Toe Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j] = new JButton("");
                buttons[i][j].setFont(new Font("Arial", Font.BOLD, 60));
                final int row = i;
                final int col = j;
                buttons[i][j].addActionListener(e -> controller.onButtonClick(row, col));
                boardPanel.add(buttons[i][j]);
            }
        }

        JPanel controlPanel = new JPanel(new FlowLayout());

        resetButton = new JButton("New Game");
        resetButton.addActionListener(e -> controller.onResetClick());

        String[] difficulties = {"Two Players", "Easy AI", "Medium AI", "Hard AI"};
        difficultyComboBox = new JComboBox<>(difficulties);
        difficultyComboBox.addActionListener(e -> {
            int selectedIndex = difficultyComboBox.getSelectedIndex();
            controller.setAiDifficulty(selectedIndex);
        });

        statusLabel = new JLabel("Player X's turn");
        statusLabel.setFont(new Font("Arial", Font.BOLD, 20));

        controlPanel.add(difficultyComboBox);
        controlPanel.add(resetButton);

        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(statusLabel, BorderLayout.NORTH);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setVisible(true);
    }

    /**
     * Updates the game board and status message after a move is made.
     *
     * @param row     the row index of the button to update
     * @param col     the column index of the button to update
     * @param symbol  the symbol ('X' or 'O') to display on the button
     * @param message the message to display in the status label
     */
    public void update(int row, int col, char symbol, String message) {
        buttons[row][col].setText(String.valueOf(symbol));
        updateMessage(message);
    }

    /**
     * Updates the status message displayed in the GUI.
     *
     * @param message the message to display in the status label
     */
    public void updateMessage(String message) {
        statusLabel.setText(message);
    }

    /**
     * Displays a game-over message and disables all buttons on the game board.
     *
     * @param message the game-over message to display
     */
    public void showGameOver(String message) {
        statusLabel.setText(message);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

    /**
     * Resets the game board and status label to their initial states.
     */
    public void resetGame() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setText("");
                buttons[i][j].setEnabled(true);
            }
        }
        statusLabel.setText("Player X's turn");
    }
}
