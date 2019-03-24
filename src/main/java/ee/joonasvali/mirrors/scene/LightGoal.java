package ee.joonasvali.mirrors.scene;


import java.awt.*;

public class LightGoal extends RoundPhysical implements LightAbsorber, Collidable {
  private LightGoalAction action;
  private final Color color = new Color(87, 107, 255);
  private final Color internalColor;

  public LightGoal(double x, double y, double rotation, double radius, LightGoalAction action, Color internal) {
    super(x, y, rotation, radius);
    this.action = action;
    this.internalColor = internal;
  }

  @Override
  public void onHit(Light light) {
    action.hit(light);
  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(color);
    g.drawOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    g.setColor(internalColor);
    g.drawOval((int)getX() + 1, (int)getY() + 1, (int)getWidth() - 1, (int)getHeight() - 1);
  }

  @Override
  public boolean isCollision(Light light) {
    return CollisionUtil.areColliding(this, light.getX(), light.getY());
  }

  @Override
  public void actCollision(Light light, Environment environment) {
    action.hit(light);
  }
}
