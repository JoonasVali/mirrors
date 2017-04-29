package ee.joonasvali.mirrors.scene;

import ee.joonasvali.mirrors.graphics.Util;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

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
    double xVector = speed * Math.cos(Math.toRadians(angle));
    double yVector = speed * Math.sin(Math.toRadians(angle));
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

    double xVector = light.getSpeed() * Math.cos(Math.toRadians(light.getAngle()));
    double yVector = light.getSpeed() * Math.sin(Math.toRadians(light.getAngle()));
    double nextX = lightX + xVector;
    double nextY = lightY + yVector;

    Point intersection = Util.getLineIntersection(lightX, lightY, nextX, nextY, x, y, x2, y2);
    if (intersection != null) {
      light.x = intersection.getX();
      light.y = intersection.getY();

      double lineAngle = Util.getAngle(x, y, x2, y2);

      double vectorAngle = Util.getAngle(lightX, lightY, nextX, nextY);
      double diff = Util.getAngleDiff(lineAngle, vectorAngle );
      System.out.println(lineAngle + " " + diff);

      if (y2 > y) {
        light.setAngle(lineAngle - diff);
      } else {
        light.setAngle(lineAngle + diff);
      }
    }
  }
}
