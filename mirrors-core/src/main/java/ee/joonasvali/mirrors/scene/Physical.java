package ee.joonasvali.mirrors.scene;

import java.awt.*;

public interface Physical {
  double getX();
  double getY();
  double getRotation();
  double getWidth();
  double getHeight();
  void render(Graphics2D g);
}
