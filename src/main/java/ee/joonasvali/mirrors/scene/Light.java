package ee.joonasvali.mirrors.scene;


import java.awt.Color;
import java.awt.Graphics2D;

public class Light extends BasePhysical implements Activatable {
  private double angle;
  private double speed;
  private double itensity;
  private final double initialItensity;
  private final double rate;

  private static Color[] colors = new Color[256];
  static {
    for (int i = 0; i < colors.length; i++) {
      colors[i] = new Color(255,255,255,i);
    }
  }

  /**
   *
   * @param x
   * @param y
   * @param angle with 0 on top
   * @param speed
   * @param itensity
   * @param itensityReductionRate
   */
  public Light(double x, double y, double angle, double speed, double itensity, double itensityReductionRate) {
    super(x, y, 0, 1, 1);
    this.itensity = itensity;
    this.initialItensity = itensity;
    this.rate = itensityReductionRate;
    this.angle = angle;
    this.speed = speed;
  }

  @Override
  public void render(Graphics2D g) {
    if (itensity < 0) {
      return;
    }
    int rate = (int) (itensity / initialItensity * 255);
    g.setColor(colors[rate]);

    g.drawLine((int)getX(), (int)getY(), (int)getX(), (int)getY());
  }

  @Override
  public void activate() {
    // This naturally produces vectors, which consider right hand
    // as 0-degree marker, not top, so we subtract the needed 90 to get 0-degree marker top.
    double xVector = speed * Math.cos(Math.toRadians(angle - 90));
    double yVector = speed * Math.sin(Math.toRadians(angle - 90));
    x += xVector;
    y += yVector;
    itensity -= rate;
  }

  public double getItensity() {
    return itensity;
  }

  public void reduceItensity(double amount){
    this.itensity = Math.max(this.itensity - amount, 0);
  }

  public double getAngle() {
    return angle;
  }

  public void setAngle(double angle) {
    this.angle = angle;
  }

  public double getSpeed() {
    return speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }
}
