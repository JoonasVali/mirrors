package ee.joonasvali.mirrors.scene;

import ee.joonasvali.mirrors.graphics.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import static java.lang.Math.atan2;

/**
 * @author Joonas Vali April 2017
 */
public class LinePhysical implements Collidable {

  private final double x;
  private final double y;
  private final double x2;
  private final double y2;

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

  public boolean isAboutToCollide(Light light) {
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
  public boolean isCollision(Physical object) {
    if (!(object instanceof Light)) {
      return false;
    }
    Light light = (Light) object;
    return isAboutToCollide(light);
  }

  @Override
  public void actCollision(Physical object, Environment environment) {
    if (!(object instanceof Light)) {
      return;
    }

    Light light = (Light) object;

    double lightX = light.getX();
    double lightY = light.getY();

    double xVector = light.getSpeed() * Math.cos(Math.toRadians(light.getAngle() - 90));
    double yVector = light.getSpeed() * Math.sin(Math.toRadians(light.getAngle() - 90));
    double nextX = lightX + xVector;
    double nextY = lightY + yVector;

    Point intersection = Util.getLineIntersection(lightX, lightY, nextX, nextY, x, y, x2, y2);
    if (intersection != null) {
      double dx=x2-x;
      double dy=y2-y;
      double normalX = -dy;
      double normalY = dx;

      //https://sinepost.wordpress.com/2012/08/30/bouncing-off-the-walls-more-productively/

      double distPerpWall = distAlong(xVector, yVector, normalX, normalY);
      double distParWall = distAlong(xVector, yVector, normalY, -normalX);

      distPerpWall = -distPerpWall;

      xVector = distParWall * normalY + distPerpWall * normalX;
      yVector = distParWall * -normalX + distPerpWall * normalY;
      double angle = Math.toDegrees(atan2(yVector,xVector)) + 90;

      light.setAngle(angle);
    }

  }

  private double distAlong(double x, double y, double xAlong, double yAlong) {
    return (x * xAlong + y * yAlong) / Math.hypot(xAlong, yAlong);
  }
}
