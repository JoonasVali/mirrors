package ee.joonasvali.mirrors.scene;


import java.awt.Color;
import java.awt.Graphics2D;

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
  public boolean isCollision(Physical light) {
    if(light instanceof Light) {
      if(CollisionUtil.areColliding(this, light.getX(), light.getY())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public void actCollision(Physical object, Environment environment) {
    if(object instanceof Light) {
      action.hit((Light)object);
      environment.remove(object);
      environment.addScore(((Light)object).getItensity());
    }
  }
}
