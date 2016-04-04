package ee.joonasvali.scene;


import org.newdawn.slick.Graphics;

public interface Physical {
  double getX();
  double getY();
  double getRotation();
  double getWidth();
  double getHeight();
  void render(Graphics g);
}
