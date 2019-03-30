package ee.joonasvali.mirrors.scene;


import java.awt.*;


public class Bender extends RoundPhysical implements Collidable {

  private double strength;

  public Bender(double x, double y, double rotation, double radius, double strength) {
    super(x, y, rotation, radius);
    this.strength = strength;
  }

  @Override
  public boolean isCollision(Light light) {
    return CollisionUtil.areColliding(this, light.getX(), light.getY());
  }

  @Override
  public void actCollision(Light light, Environment environment) {
    light.setAngle(light.getAngle() + strength);
  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(Color.pink);
    g.drawOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    g.drawString(String.format("%.1f", strength), (float)getCenterX() - 10, (float)getCenterY() - 10);
  }
}
