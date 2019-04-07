package ee.joonasvali.mirrors.scene;


import java.awt.*;


public class Bender extends RoundPhysical implements Collidable {

  private double strength;

  public Bender(double x, double y, double rotation, double radius, double strength) {
    super(x, y, rotation, radius);
    this.strength = strength;
  }

  @Override
  public boolean isCollision(Particle particle) {
    return CollisionUtil.areColliding(this, particle.getX(), particle.getY());
  }

  @Override
  public void actCollision(Particle particle, Model model) {
    particle.setAngle(particle.getAngle() + strength);
  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(Color.pink);
    g.drawOval((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
    g.drawString(String.format("%.1f", strength), (float) getCenterX() - 10, (float) getCenterY() - 10);
  }
}
