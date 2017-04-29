package ee.joonasvali.mirrors.graphics;

import ee.joonasvali.mirrors.scene.Environment;

import java.awt.Graphics2D;

public interface Renderer {
  void render(Environment environment, Graphics2D g);
}
