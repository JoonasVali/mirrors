package ee.joonasvali.scene;


import java.awt.Color;
import java.awt.Graphics2D;


public class Bender extends RoundPhysical implements Collidable {

  private double delta;

  public Bender(double x, double y, double rotation, double radius, double delta) {
    super(x, y, rotation, radius);
    this.delta = delta;
  }

  @Override
  public boolean isCollision(Physical object) {
    if (object instanceof Light) {
      Light light = (Light) object;
      return CollisionUtil.areColliding(this, light.getX(), light.getY());
    }
    return false;
  }

  @Override
  public void actCollision(Physical object, Environment environment) {
    if (object instanceof Light) {
      Light light = (Light) object;
      light.setAngle(light.getAngle() + delta);
    }
  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(Color.pink);
    g.drawOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    g.drawString(String.format("%.1f", delta), (float)getCenterX() - 10, (float)getCenterY() - 10);
  }
}
