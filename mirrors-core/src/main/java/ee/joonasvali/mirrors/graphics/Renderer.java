package ee.joonasvali.mirrors.graphics;

import ee.joonasvali.mirrors.scene.Model;

import java.awt.*;

public interface Renderer {
  void render(Model model, Graphics2D g);
}
