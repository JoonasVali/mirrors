package ee.joonasvali.mirrors.scene;


import java.awt.*;

public class LightGoal extends RoundPhysical implements LightAbsorber, Collidable {
  private LightGoalAction action;
  private Color color = new Color(87, 107, 255);
  public LightGoal(double x, double y, double rotation, double radius, LightGoalAction action) {
    super(x, y, rotation, radius);
    this.action = action;
  }

  @Override
  public void onHit(Light light) {
    action.hit(light);
  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(color);
    g.drawOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
  }

  @Override
  public boolean isCollision(Light light) {
    return CollisionUtil.areColliding(this, light.getX(), light.getY());
  }

  @Override
  public void actCollision(Light light, Environment environment) {
    action.hit(light);
    environment.remove(light);
    environment.addScore((light).getItensity());
  }
}
