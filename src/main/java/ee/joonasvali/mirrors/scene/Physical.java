package ee.joonasvali.mirrors.scene;
import java.awt.Graphics2D;

public interface Physical {
  double getX();
  double getY();
  double getRotation();
  double getWidth();
  double getHeight();
  void render(Graphics2D g);
}
