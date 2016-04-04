package ee.joonasvali.scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: Joss
 * Date: 3/23/14
 * Time: 3:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class LightGoal extends RoundPhysical implements LightAbsorber, Collidable {
  private LightGoalAction action;
  public LightGoal(double x, double y, double rotation, double radius, LightGoalAction action) {
    super(x, y, rotation, radius);
    this.action = action;
  }

  @Override
  public void onHit(Light light) {
    action.hit(light);
  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.blue);
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
