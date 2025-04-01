import javax.swing.*;

// Model.java
public class Model {
    private View view;
    private int currentPlayer; // 1 for X, 2 for O
    private int movesCount;
    private char[][] board;
    private String message;
    private boolean gameOver;
    private int aiDifficulty; // 0: No AI, 1: Easy, 2: Medium, 3: Hard

    public Model() {
        board = new char[3][3];
        resetModel();
    }

    public void registerView(View view) {
        this.view = view;
    }

    public View getView() {
        return view;
    }

    public void resetModel() {
        currentPlayer = 1; // X starts
        movesCount = 0;
        gameOver = false;
        message = "Player X's turn";

        // Initialize empty board
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }

        // Update view if it exists
        if (view != null) {
            view.resetGame();
        }
    }

    public void setAiDifficulty(int difficulty) {
        this.aiDifficulty = difficulty;
    }

    public void playMove(int row, int col) {
        if (gameOver || row < 0 || row >= 3 || col < 0 || col >= 3 || board[row][col] != ' ') {
            return;
        }

        char symbol = (currentPlayer == 1) ? 'X' : 'O';
        board[row][col] = symbol;
        movesCount++;

        view.update(row, col, symbol, message);

        if (checkWinner(row, col)) {
            message = "Player " + symbol + " wins!";
            gameOver = true;
            view.showGameOver(message);
        } else if (movesCount == 9) {
            message = "Game ended in a tie!";
            gameOver = true;
            view.showGameOver(message);
        } else {
            currentPlayer = (currentPlayer == 1) ? 2 : 1;
            symbol = (currentPlayer == 1) ? 'X' : 'O';
            message = "Player " + symbol + "'s turn";
            view.updateMessage(message);

            // If AI's turn and game not over
            if (currentPlayer == 2 && aiDifficulty > 0 && !gameOver) {
                makeAiMove();
            }
        }
    }

    private void makeAiMove() {
        // Delay AI move slightly for better UX
        new Thread(() -> {
            try {
                Thread.sleep(500);
                int[] move = new int[2];

                switch (aiDifficulty) {
                    case 1: // Easy - Random move
                        move = makeRandomMove();
                        break;
                    case 2: // Medium - Can block and win
                        move = makeMediumMove();
                        break;
                    case 3: // Hard - Optimal play
                        move = makeHardMove();
                        break;
                }

                if (move != null) {
                    final int row = move[0];
                    final int col = move[1];

                    SwingUtilities.invokeLater(() -> playMove(row, col));
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private int[] makeRandomMove() {
        java.util.List<int[]> availableMoves = new java.util.ArrayList<>();

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    availableMoves.add(new int[]{i, j});
                }
            }
        }

        if (availableMoves.isEmpty()) {
            return null;
        }

        int randomIndex = (int)(Math.random() * availableMoves.size());
        return availableMoves.get(randomIndex);
    }

    private int[] makeMediumMove() {
        // First check if AI can win
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'O';
                    if (checkWinner(i, j)) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }

        // Then check if player can win and block
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = 'X';
                    if (checkWinner(i, j)) {
                        board[i][j] = ' ';
                        return new int[]{i, j};
                    }
                    board[i][j] = ' ';
                }
            }
        }

        // Otherwise make a random move
        return makeRandomMove();
    }

    private int[] makeHardMove() {
        // Implementation of minimax algorithm for optimal play
        int[] bestMove = minimax(2, 'O');
        return new int[]{bestMove[1], bestMove[2]};
    }

    private int[] minimax(int depth, char player) {
        int[] best = new int[]{(player == 'O') ? Integer.MIN_VALUE : Integer.MAX_VALUE, -1, -1};

        if (checkGameOver() || depth == 0) {
            int score = evaluateBoard();
            return new int[]{score, -1, -1};
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    board[i][j] = player;

                    int[] score;
                    if (player == 'O') {
                        score = minimax(depth - 1, 'X');
                        if (score[0] > best[0]) {
                            best[0] = score[0];
                            best[1] = i;
                            best[2] = j;
                        }
                    } else {
                        score = minimax(depth - 1, 'O');
                        if (score[0] < best[0]) {
                            best[0] = score[0];
                            best[1] = i;
                            best[2] = j;
                        }
                    }

                    board[i][j] = ' ';
                }
            }
        }

        return best;
    }

    private int evaluateBoard() {
        // Check rows, columns, and diagonals for winner
        for (int i = 0; i < 3; i++) {
            // Check rows
            if (board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                if (board[i][0] == 'O') return 10;
                else if (board[i][0] == 'X') return -10;
            }

            // Check columns
            if (board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                if (board[0][i] == 'O') return 10;
                else if (board[0][i] == 'X') return -10;
            }
        }

        // Check diagonals
        if (board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            if (board[0][0] == 'O') return 10;
            else if (board[0][0] == 'X') return -10;
        }

        if (board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            if (board[0][2] == 'O') return 10;
            else if (board[0][2] == 'X') return -10;
        }

        return 0; // No winner
    }

    private boolean checkGameOver() {
        // Check if game is over (win or tie)
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] != ' ' && checkWinner(i, j)) {
                    return true;
                }
            }
        }

        // Check for tie
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') {
                    return false; // Game still in progress
                }
            }
        }

        return true; // Tie
    }

    public boolean checkWinner(int row, int col) {
        char symbol = board[row][col];
        if (symbol == ' ') return false;

        // Check row
        boolean win = true;
        for (int j = 0; j < 3; j++) {
            if (board[row][j] != symbol) {
                win = false;
                break;
            }
        }
        if (win) return true;

        // Check column
        win = true;
        for (int i = 0; i < 3; i++) {
            if (board[i][col] != symbol) {
                win = false;
                break;
            }
        }
        if (win) return true;

        // Check diagonals
        if (row == col) {
            win = true;
            for (int i = 0; i < 3; i++) {
                if (board[i][i] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        if (row + col == 2) {
            win = true;
            for (int i = 0; i < 3; i++) {
                if (board[i][2-i] != symbol) {
                    win = false;
                    break;
                }
            }
            if (win) return true;
        }

        return false;
    }

    public boolean isGameOver() {
        return gameOver;
    }
}
