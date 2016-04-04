package ee.joonasvali.scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: Joss
 * Date: 3/25/14
 * Time: 8:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class Accelerator extends RoundPhysical implements Collidable{
  private double acceleration;

  public Accelerator(double x, double y, double rotation, double radius, double acceleration) {
    super(x, y, rotation, radius);
    this.acceleration = acceleration;
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
      light.setSpeed(light.getSpeed() + acceleration);
    }
  }

  @Override
  public void render(Graphics g) {
    if(acceleration > 0) {
      g.setColor(Color.green);
    } else g.setColor(Color.yellow);
    g.drawOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    g.drawString(String.format("%.1f", acceleration), (float)getCenterX() - 10, (float)getCenterY() - 10);
  }
}
