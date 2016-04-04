package ee.joonasvali;

import org.newdawn.slick.Graphics;

/**
 * @author Joonas Vali April 2016
 */
public interface EnvironmentController {
  void init();

  void render(Graphics g);

  void update(int delta);

  void keyPressed(int key, char c);

  void keyReleased(int key, char c);
}
