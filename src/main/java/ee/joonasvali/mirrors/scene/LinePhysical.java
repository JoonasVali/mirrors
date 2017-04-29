package ee.joonasvali.mirrors.scene;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * @author Joonas Vali April 2017
 */
public class LinePhysical {

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
}
