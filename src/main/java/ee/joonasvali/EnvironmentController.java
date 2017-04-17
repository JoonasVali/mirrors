package ee.joonasvali;

import ee.joonasvali.scene.Environment;

import java.awt.Graphics2D;

/**
 * @author Joonas Vali April 2016
 */
public interface EnvironmentController {
  void init();

  void render(Graphics2D g);

  void keyPressed(int key, char c);

  void keyReleased(int key, char c);

  void nextStep();

  Environment getEnvironment();
}
