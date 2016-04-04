package ee.joonasvali.graphics;

import ee.joonasvali.scene.Environment;
import org.newdawn.slick.Graphics;

/**
 * @author Joonas Vali April 2016
 */
public interface Renderer {
  void render(Environment environment, Graphics g);
}
