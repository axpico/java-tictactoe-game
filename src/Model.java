import javax.swing.*;

/**
 * Represents the model for a Tic-Tac-Toe game, including game logic and AI functionality.
 */
public class Model {
    private View view;
    private int currentPlayer; // 1 for X, 2 for O
    private int movesCount;
    private final char[][] board;
    private String message;
    private boolean gameOver;
    private int aiDifficulty; // 0: No AI, 1: Easy, 2: Medium, 3: Hard

    /**
     * Initializes the model and resets the game state.
     */
    public Model() {
        board = new char[3][3];
        resetModel();
    }

    /**
     * Registers the view associated with this model.
     *
     * @param view The view to be registered.
     */
    public void registerView(View view) {
        this.view = view;
    }

    /**
     * Resets the game state to its initial configuration.
     */
    public void resetModel() {
        currentPlayer = 1;
        movesCount = 0;
        gameOver = false;
        message = "Player X's turn";

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }

        if (view != null) {
            view.resetGame();
        }
    }

    /**
     * Sets the difficulty level of the AI.
     *
     * @param difficulty The difficulty level (0: No AI, 1: Easy, 2: Medium, 3: Hard).
     */
    public void setAiDifficulty(int difficulty) {
        this.aiDifficulty = difficulty;
    }

    /**
     * Processes a player's move and updates the game state accordingly.
     *
     * @param row The row index of the move.
     * @param col The column index of the move.
     */
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

            if (currentPlayer == 2 && aiDifficulty > 0 && !gameOver) {
                makeAiMove();
            }
        }
    }

    /**
     * Executes an AI move based on the set difficulty level.
     */
    private void makeAiMove() {
        new Thread(() -> {
            try {
                Thread.sleep(500);
                int[] move = new int[2];

                move = switch (aiDifficulty) {
                    case 1 -> makeRandomMove();
                    case 2 -> makeMediumMove();
                    case 3 -> makeHardMove();
                    default -> move;
                };

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

    /**
     * Generates a random valid move for the AI.
     *
     * @return An array containing the row and column indices of the move.
     */
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

    /**
     * Generates a medium-difficulty move for the AI by blocking or winning when possible.
     *
     * @return An array containing the row and column indices of the move.
     */
    private int[] makeMediumMove() {
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

        return makeRandomMove();
    }

    /**
     * Generates a hard-difficulty move using optimal play strategies such as minimax.
     *
     * @return An array containing the row and column indices of the move.
     */
    private int[] makeHardMove() {
        int[] bestMove = minimax(2, 'O');
        return new int[]{bestMove[1], bestMove[2]};
    }

    /**
     * Implements the minimax algorithm to determine optimal moves.
     *
     * @param depth The depth of recursion for evaluating moves.
     * @param player The current player ('X' or 'O').
     * @return An array containing score and indices of the best move.
     */
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

    /**
     * Checks if the game is over, either by a win or a tie.
     *
     * @return True if the game is over, false otherwise.
     */
    private boolean checkGameOver() {
        return gameOver || movesCount == 9;
    }

    /**
     * Checks if the current move results in a win for the current player.
     *
     * @param row The row index of the last move.
     * @param col The column index of the last move.
     * @return True if the current player wins, false otherwise.
     */
    private boolean checkWinner(int row, int col) {
        char symbol = board[row][col];

        // Check row
        if (board[row][0] == symbol && board[row][1] == symbol && board[row][2] == symbol) {
            return true;
        }

        // Check column
        if (board[0][col] == symbol && board[1][col] == symbol && board[2][col] == symbol) {
            return true;
        }

        // Check main diagonal
        if (row == col && board[0][0] == symbol && board[1][1] == symbol && board[2][2] == symbol) {
            return true;
        }

        // Check anti-diagonal
        return row + col == 2 && board[0][2] == symbol && board[1][1] == symbol && board[2][0] == symbol;
    }

    /**
     * Evaluates the current state of the board to calculate a score for minimax.
     *
     * @return A score representing how favorable the board is for the AI player.
     */
    private int evaluateBoard() {
        for (int i = 0; i < 3; i++) {
            // Check rows for a win
            if (board[i][0] != ' ' && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                return (board[i][0] == 'O') ? 10 : -10;
            }
            // Check columns for a win
            if (board[0][i] != ' ' && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                return (board[0][i] == 'O') ? 10 : -10;
            }
        }

        // Check main diagonal for a win
        if (board[0][0] != ' ' && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            return (board[0][0] == 'O') ? 10 : -10;
        }

        // Check anti-diagonal for a win
        if (board[0][2] != ' ' && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            return (board[0][2] == 'O') ? 10 : -10;
        }

        // If no winner, return 0
        return 0;
    }

    /**
     * Returns whether the game has ended or not.
     *
     * @return True if the game is over, false otherwise.
     */
    public boolean isGameOver() {
        return this.gameOver;
    }

}