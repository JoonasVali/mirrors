package ee.joonasvali.mirrors.graphics;

import ee.joonasvali.mirrors.EnvironmentController;

import javax.swing.*;
import java.awt.*;

/**
 * @author Joonas Vali April 2017
 */
public class GameContainer extends JFrame {
  private final EnvironmentController env;
  private final JPanel canvas;

  public GameContainer(EnvironmentController env, int width, int height) {
    this.env = env;
    this.setLayout(new BorderLayout());
    this.canvas = createCanvas();
    this.canvas.setSize(width, height);
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