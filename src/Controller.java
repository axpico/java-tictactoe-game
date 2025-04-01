/**
 * The Controller class acts as an intermediary between the view and the model,
 * handling user interactions and updating the model accordingly.
 */
public class Controller {
    private Model model;

    /**
     * Sets the model for this controller.
     *
     * @param model the model to be associated with this controller
     */
    public void setModel(Model model) {
        this.model = model;
    }

    /**
     * Handles the action when a button is clicked.
     * If the game is not over, it plays a move at the specified row and column.
     *
     * @param row the row index of the move
     * @param col the column index of the move
     */
    public void onButtonClick(int row, int col) {
        if (!model.isGameOver()) {
            model.playMove(row, col);
        }
    }

    /**
     * Handles the action when the reset button is clicked.
     * Resets the model to its initial state.
     */
    public void onResetClick() {
        model.resetModel();
    }

    /**
     * Sets the AI difficulty level in the model.
     * If the game is not over, it resets the game after changing the difficulty.
     *
     * @param difficulty the difficulty level to set for the AI
     */
    public void setAiDifficulty(int difficulty) {
        model.setAiDifficulty(difficulty);
        if (!model.isGameOver()) {
            onResetClick();
        }
    }
}
