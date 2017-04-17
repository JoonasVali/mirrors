package ee.joonasvali.graphics;

import ee.joonasvali.EnvironmentController;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

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
    return new JPanel() {
      @Override
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
        env.render((Graphics2D) g);
      }
    };
  }
}
