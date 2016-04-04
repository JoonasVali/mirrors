package ee.joonasvali.scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: Joss
 * Date: 3/23/14
 * Time: 6:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class Reflector extends BasePhysical implements Collidable {

  public static final double REFLECTION_DELTA = 1;
  private static final double REDUCE_ON_HIT = 2;

  public Reflector(double x, double y, double rotation, double width, double height) {
    super(x, y, rotation, width, height);

  }

  @Override
  public void render(Graphics g) {
    g.setColor(Color.cyan);
    g.drawRect((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());

  }

  @Override
  public boolean isCollision(Physical object) {
    return CollisionUtil.areColliding(this, object);

  }

  @Override
  public void actCollision(Physical object, Environment environment) {

    if(object instanceof Light) {
      Light light = (Light) object;
      double angle = light.getAngle();
      double xUnitVector = Math.cos(Math.toRadians(angle));
      double yUnitVector = Math.sin(Math.toRadians(angle));

      light.reduceItensity(REDUCE_ON_HIT);

      Boolean reverseX = (isReverseX(light));
      if(reverseX != null) {
        if(reverseX){
          xUnitVector = -xUnitVector;
        } else {
          yUnitVector = -yUnitVector;
        }
      } else {
        light.setAngle(angle + 45);
        return;
      }

      angle = Math.toDegrees(Math.atan2(yUnitVector, xUnitVector));
      light.setAngle(angle);
    }
  }

  private Boolean isReverseX(Light light) {
    double distanceLeft = Math.abs(light.getX() - this.getX());

    double smallest = distanceLeft;
    boolean reverse = true;

    double distanceRight = Math.abs(light.getX() - (this.getX() + this.getWidth() ));
    if(Math.abs(smallest - distanceRight) < REFLECTION_DELTA) return null;
    if(smallest > distanceRight) {
      smallest = distanceRight;
    }
    double distanceTop = Math.abs(light.getY() - (this.getY()));
    if(Math.abs(smallest - distanceTop) < REFLECTION_DELTA) return null;
    if(smallest > distanceTop) {
      reverse = false;
      smallest = distanceTop;
    }
    double distanceBottom = Math.abs(light.getY() - (this.getY() + this.getHeight() ));
    if(Math.abs(smallest - distanceBottom) < REFLECTION_DELTA) return null;
    if(smallest > distanceBottom) {
      reverse = false;
    }
    return reverse;
  }
}
