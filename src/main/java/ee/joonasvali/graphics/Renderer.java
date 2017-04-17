package ee.joonasvali.graphics;

import ee.joonasvali.scene.Environment;

import java.awt.Graphics2D;

public interface Renderer {
  void render(Environment environment, Graphics2D g);
}
