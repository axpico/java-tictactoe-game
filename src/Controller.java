public class Controller {
    private Model model;

    public void setModel(Model model) {
        this.model = model;
    }

    public void onButtonClick(int row, int col) {
        if (!model.isGameOver()) {
            model.playMove(row, col);
        }
    }

    public void onResetClick() {
        model.resetModel();
    }

    public void setAiDifficulty(int difficulty) {
        model.setAiDifficulty(difficulty);
        // Reset game when changing difficulty
        if (!model.isGameOver()) {
            onResetClick();
        }
    }
}
