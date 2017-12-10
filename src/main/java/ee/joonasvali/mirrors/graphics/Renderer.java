package ee.joonasvali.mirrors.graphics;

import ee.joonasvali.mirrors.scene.Environment;

import java.awt.*;

public interface Renderer {
  void render(Environment environment, Graphics2D g);
}
