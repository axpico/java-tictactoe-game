import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Model model = new Model();
            View view = new View();
            Controller controller = new Controller();

            model.registerView(view);
            controller.setModel(model);
            view.setActionListener(controller);

            view.initialize();
        });
    }
}
