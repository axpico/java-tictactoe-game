// Controller.java
public class Controller {
    private Model model;

    public void setModel(Model model) {
        this.model = model;
    }

    public void onButtonClick(int row, int col) {
        model.playMove(row, col);
    }

    public void onResetClick() {
        model.resetModel();
        model.registerView(model.getView());
    }

    public void setAiDifficulty(int difficulty) {
        model.setAiDifficulty(difficulty);
        // Reset game when changing difficulty
        if (!model.isGameOver()) {
            onResetClick();
        }
    }
}
