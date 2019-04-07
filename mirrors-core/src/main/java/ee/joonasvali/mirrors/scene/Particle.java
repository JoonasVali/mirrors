package ee.joonasvali.mirrors.scene;


import java.awt.*;

public class Particle extends BasePhysical implements Activatable {
  private double angle;
  private double speed;
  private double intensity;
  private final double initialIntensity;
  private final double rate;
  private final ParticleGroup particleGroup;

  /**
   * @param x
   * @param y
   * @param angle                 with 0 on top
   * @param speed
   * @param intensity
   * @param itensityReductionRate
   */
  public Particle(
      double x, double y, double angle, double speed, double intensity, double itensityReductionRate, ParticleGroup group
  ) {
    super(x, y, 0, 1, 1);
    this.intensity = intensity;
    this.initialIntensity = intensity;
    this.rate = itensityReductionRate;
    this.angle = angle;
    this.speed = speed;
    this.particleGroup = group;
  }

  @Override
  public void render(Graphics2D g) {
    particleGroup.render(g, getX(), getY(), intensity, initialIntensity);
  }

  @Override
  public void activate() {
    // This naturally produces vectors, which consider right hand
    // as 0-degree marker, not top, so we subtract the needed 90 to get 0-degree marker top.
    double xVector = speed * Math.cos(Math.toRadians(angle - 90));
    double yVector = speed * Math.sin(Math.toRadians(angle - 90));
    x += xVector;
    y += yVector;
    intensity -= rate;
  }

  public double getIntensity() {
    return intensity;
  }

  public void reduceItensity(double amount) {
    this.intensity = Math.max(this.intensity - amount, 0);
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

  public ParticleGroup getParticleGroup() {
    return particleGroup;
  }
}
