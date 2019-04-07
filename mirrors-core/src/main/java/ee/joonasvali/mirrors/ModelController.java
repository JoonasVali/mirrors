package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.scene.Model;

import java.awt.*;
import java.awt.event.KeyListener;

/**
 * @author Joonas Vali April 2016
 */
public interface ModelController {
  void init();

  void render(Graphics2D g);

  void nextStep();

  Model getModel();

  KeyListener getKeyListener();
}
