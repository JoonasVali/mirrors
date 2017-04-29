package ee.joonasvali.mirrors.graphics;


import java.awt.Point;

public class Util {
  public static double getDistance(double x1, double y1, double x2, double y2) {
    return Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
  }

  public static double getAngleDiff(double first, double second) {
    double diff = first - second;
    while (diff > 180) {
      diff -= 360;
    }
    while (diff < -180) {
      diff += 360;
    }
    return diff;
  }

  /**
   * @return angle in degrees, with 0 top
   */
  public static float getAngle(double x, double y, double x2, double y2) {
    float angle = (float) Math.toDegrees(Math.atan2(x2 - x, y2 - y));

    while (angle < 0){
      angle += 360;
    }

    return angle;
  }

  public static Point trajectory(double x, double y, double angle, double velocity, long timeDiff) {
    if (timeDiff < 0) {
      throw new RuntimeException("This is not acceptable diff: " + timeDiff);
    }

    int resultX = (int) (x + (timeDiff * velocity) * Math.sin(Math.toRadians(angle)));
    int resultY = (int) (y + (timeDiff * velocity) * Math.cos(Math.toRadians(angle)));

    return new Point(resultX,resultY);
  }

  public static boolean isInRange(double x, double y, double x2, double y2, double radius) {
    return getDistance(x, y, x2, y2) < radius;
  }


  public static boolean doubleEqual(Double a, Double b, double delta) {
    return Math.abs(a - b) < delta;
  }

  public static boolean isPointOnStraightLine(double x, double y, double x2, double y2, double pointX, double pointY, double detectRadius) {
    if (doubleEqual(x, x2, 0.0000000001)) {
      return !doubleEqual(y, y2, 0.0000000001) && doubleEqual(pointX, x, detectRadius);
    }
    double k = (y - y2) / (x - x2);
    double hypotheticalY = (pointX * k) + y - (k * x);
    return doubleEqual(hypotheticalY, pointY, detectRadius);
  }


  /**
   * Taken from http://stackoverflow.com/a/14795484
   */
  public static Point getLineIntersection(double p0_x, double p0_y, double p1_x, double p1_y,
                                   double p2_x, double p2_y, double p3_x, double p3_y) {
    double s02_x, s02_y, s10_x, s10_y, s32_x, s32_y, s_numer, t_numer, denom, t;
    s10_x = p1_x - p0_x;
    s10_y = p1_y - p0_y;
    s32_x = p3_x - p2_x;
    s32_y = p3_y - p2_y;

    denom = s10_x * s32_y - s32_x * s10_y;
    if (denom == 0)
      return null; // Collinear
    boolean denomPositive = denom > 0;

    s02_x = p0_x - p2_x;
    s02_y = p0_y - p2_y;
    s_numer = s10_x * s02_y - s10_y * s02_x;
    if ((s_numer < 0) == denomPositive)
      return null; // No collision

    t_numer = s32_x * s02_y - s32_y * s02_x;
    if ((t_numer < 0) == denomPositive)
      return null; // No collision

    if (((s_numer >= denom) == denomPositive) || ((t_numer >= denom) == denomPositive))
      return null; // No collision
    // Collision detected
    t = t_numer / denom;
    Point p = new Point((int)(p0_x + (t * s10_x)), (int)(p0_y + (t * s10_y)));
    return p;
  }


  public static boolean isIntersecting(double aX, double aY, double bX, double bY, double cX, double cY, double dX, double dY) {
    double denominator = ((bX - aX) * (dY - cY)) - ((bY - aY) * (dX - cX));
    double numerator1 = ((aY - cY) * (dX - cX)) - ((aX - cX) * (dY - cY));
    double numerator2 = ((aY - cY) * (bX - aX)) - ((aX - cX) * (bY - aY));

    // Detect coincident lines
    if (denominator == 0) {
      return numerator1 == 0 && numerator2 == 0;
    }

    double r = numerator1 / denominator;
    double s = numerator2 / denominator;

    return (r >= 0 && r <= 1) && (s >= 0 && s <= 1);
  }

}
