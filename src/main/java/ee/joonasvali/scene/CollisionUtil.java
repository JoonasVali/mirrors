package ee.joonasvali.scene;


public class CollisionUtil {

  public static boolean areColliding(RoundPhysical c1, RoundPhysical c2) {
    double center_distance = Math.sqrt(Math.pow(c1.getCenterX() - c2.getCenterX(), 2) + Math.pow(c1.getCenterY() - c2.getCenterY(), 2));
    return (c1.getRadius() + c2.getRadius()) < center_distance;
  }

  public static boolean areColliding(RoundPhysical c1, double x, double y) {
    double dist = Math.sqrt(Math.pow(c1.getCenterX() - x, 2) + Math.pow(c1.getCenterY() - y, 2));
    return dist < c1.getRadius();
  }

  // Rectangle check with point
  public static boolean areColliding(Physical c1, double x, double y) {
    if(c1 instanceof RoundPhysical) {
      return areColliding((RoundPhysical)c1, x, y);
    }
    return(x >= c1.getX() && x <= c1.getX() + c1.getWidth() &&
            y >= c1.getY() && y <= c1.getY() + c1.getHeight());
  }

  public static boolean areColliding(Physical c1, Physical c2) {
    if(c1 instanceof RoundPhysical && c2 instanceof RoundPhysical) {
      return areColliding((RoundPhysical)c1, (RoundPhysical)c2);
    }

    // Optimization by eliminating impossible:
    if(c1.getX() + c1.getWidth() < c2.getX() || c2.getX() + c2.getWidth() < c1.getX()){
      // The objects are not near each other X-wise.
      return false;
    }
    if(c1.getY() + c1.getHeight() < c2.getY() || c2.getY() + c2.getHeight() < c1.getY()){
      // The objects are not near each other Y-wise.
      return false;
    }

    return true;

  }
}
