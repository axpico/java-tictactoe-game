// View.java
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class View {
    private JFrame frame;
    private JButton[][] buttons;
    private JButton resetButton;
    private JLabel statusLabel;
    private JComboBox<String> difficultyComboBox;
    private Controller controller;

    public View() {
        buttons = new JButton[3][3];
    }

    public void setActionListener(Controller controller) {
        this.controller = controller;
    }

    public void initialize() {
        frame = new JFrame("Tic-Tac-Toe Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        frame.setLayout(new BorderLayout());

        // Game board panel
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

        // Control panel
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

    public void update(int row, int col, char symbol, String message) {
        buttons[row][col].setText(String.valueOf(symbol));
        updateMessage(message);
    }

    public void updateMessage(String message) {
        statusLabel.setText(message);
    }

    public void showGameOver(String message) {
        statusLabel.setText(message);

        // Disable all buttons when game is over
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                buttons[i][j].setEnabled(false);
            }
        }
    }

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
