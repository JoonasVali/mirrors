package ee.joonasvali.mirrors.scene;

import java.awt.*;
import java.util.Set;

import static java.lang.Math.atan2;

public class LineReflector extends LinePhysical {
  private Set<Integer> reflectiveGroupIds;

  public LineReflector(double x, double y, double x2, double y2, Set<Integer> reflectiveGroupIds) {
    super(x, y, x2, y2);
    this.reflectiveGroupIds = reflectiveGroupIds;
  }

  public LineReflector(double x, double y, double x2, double y2) {
    super(x, y, x2, y2);
    // Reflect all
    reflectiveGroupIds = null;
  }

  @Override
  protected void runLightCollisionAction(Light light, double lightXVector, double lightYVector) {
    if (reflectiveGroupIds != null && !reflectiveGroupIds.contains(light.getLightGroup().getId())) {
      // This reflector is transparent to this light group.
      return;
    }
    double dx=x2-x;
    double dy=y2-y;
    double normalX = -dy;
    double normalY = dx;

    //https://sinepost.wordpress.com/2012/08/30/bouncing-off-the-walls-more-productively/

    double distPerpWall = distAlong(lightXVector, lightYVector, normalX, normalY);
    double distParWall = distAlong(lightXVector, lightYVector, normalY, -normalX);

    distPerpWall = -distPerpWall;

    lightXVector = distParWall * normalY + distPerpWall * normalX;
    lightYVector = distParWall * -normalX + distPerpWall * normalY;
    double angle = Math.toDegrees(atan2(lightYVector, lightXVector)) + 90;

    light.setAngle(angle);
  }

  public void render(Graphics2D g) {
    super.render(g);
  }

  private double distAlong(double x, double y, double xAlong, double yAlong) {
    return (x * xAlong + y * yAlong) / Math.hypot(xAlong, yAlong);
  }
}
