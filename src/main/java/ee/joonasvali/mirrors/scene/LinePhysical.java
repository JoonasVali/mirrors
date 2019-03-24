package ee.joonasvali.mirrors.scene;

import ee.joonasvali.mirrors.graphics.Util;

import java.awt.*;

import static java.lang.Math.atan2;

/**
 * @author Joonas Vali April 2017
 */
public abstract class LinePhysical implements Collidable {

  protected final double x;
  protected final double y;
  protected final double x2;
  protected final double y2;

  public LinePhysical(double x, double y, double x2, double y2) {
    this.x = x;
    this.y = y;
    this.x2 = x2;
    this.y2 = y2;
  }

  public void render(Graphics2D g) {
    g.setColor(Color.cyan);
    g.drawLine((int)x, (int)y, (int)x2, (int)y2);
  }

  private boolean isAboutToCollide(Light light) {
    double lightX = light.getX();
    double lightY = light.getY();
    double speed = light.getSpeed();
    double angle = light.getAngle();
    double xVector = speed * Math.cos(Math.toRadians(angle - 90));
    double yVector = speed * Math.sin(Math.toRadians(angle - 90));
    double nextX = lightX + xVector;
    double nextY = lightY + yVector;
    return Util.isIntersecting(lightX, lightY, nextX, nextY, x, y, x2, y2);
  }


  @Override
  public boolean isCollision(Light light) {
    return isAboutToCollide(light);
  }

  @Override
  public void actCollision(Light light, Environment environment) {
    double lightX = light.getX();
    double lightY = light.getY();

    double xVector = light.getSpeed() * Math.cos(Math.toRadians(light.getAngle() - 90));
    double yVector = light.getSpeed() * Math.sin(Math.toRadians(light.getAngle() - 90));
    double nextX = lightX + xVector;
    double nextY = lightY + yVector;

    Point intersection = Util.getLineIntersection(lightX, lightY, nextX, nextY, x, y, x2, y2);
    if (intersection != null) {
      runLightCollisionAction(light, xVector, yVector);
    }

  }

  abstract void runLightCollisionAction(Light light, double lightXVector, double lightYVector);

}
