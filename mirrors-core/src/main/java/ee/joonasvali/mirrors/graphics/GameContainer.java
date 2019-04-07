package ee.joonasvali.mirrors.graphics;

import ee.joonasvali.mirrors.ModelController;

import javax.swing.*;
import java.awt.*;

/**
 * @author Joonas Vali April 2017
 */
public class GameContainer extends JFrame {
  private final ModelController env;
  private final JPanel canvas;

  public GameContainer(ModelController env) {
    this.env = env;
    this.setLayout(new BorderLayout());
    this.canvas = createCanvas();
    this.canvas.setBorder(BorderFactory.createLineBorder(Color.black));
    this.add(canvas, BorderLayout.CENTER);
  }

  private JPanel createCanvas() {
    JPanel panel = new JPanel() {
      @Override
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
        env.render((Graphics2D) g);
      }
    };
    addKeyListener(env.getKeyListener());
    return panel;
  }
}
