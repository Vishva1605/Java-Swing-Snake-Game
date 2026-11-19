import javax.swing.JFrame;

public class GameFrame extends JFrame {

    public GameFrame() {
        this.add(new GamePanel());
        this.setTitle("Snake Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.pack();                       // Fit frame to GamePanel preferred size
        this.setLocationRelativeTo(null);  // Center window on screen
        this.setVisible(true);
    }
}
