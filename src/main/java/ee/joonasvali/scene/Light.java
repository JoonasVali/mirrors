package ee.joonasvali.scene;


import java.awt.Color;
import java.awt.Graphics2D;

public class Light extends BasePhysical implements Activatable {
  private double angle;
  private double speed;
  private double itensity;
  private final double initialItensity;
  private final double rate;
  private Itensity0Action action;

  private static Color[] colors = new Color[256];
  static {
    for (int i = 0; i < colors.length; i++) {
      colors[i] = new Color(255,255,255,i);
    }
  }



  public Light(double x, double y, double angle, double speed, double itensity, double itensityReductionRate, Itensity0Action onItensity0) {
    super(x, y, 0, 1, 1);
    this.itensity = itensity;
    this.initialItensity = itensity;
    this.rate = itensityReductionRate;
    this.angle = angle;
    this.speed = speed;
    this.action = onItensity0;
  }

  @Override
  public void render(Graphics2D g) {
    int rate = (int) (itensity / initialItensity * 255);
    g.setColor(colors[rate]);
    g.drawRect((int)getX(), (int)getY(), 1, 1);
  }

  @Override
  public void activate() {
    double xVector = speed * Math.cos(Math.toRadians(angle));
    double yVector = speed * Math.sin(Math.toRadians(angle));
    x += xVector;
    y += yVector;
    itensity -= rate;
    if(itensity <= 0) action.onItensity0(this);
  }

  public double getItensity() {
    return itensity;
  }

  public void reduceItensity(double amount){
    this.itensity = Math.max(this.itensity - amount, 0);
    if(itensity <= 0) action.onItensity0(this);
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
    if(this.speed < 0) { this.speed = 0;
      action.onItensity0(this);
    }
  }
}
