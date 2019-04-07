package ee.joonasvali.mirrors.scene;


import java.awt.*;

public class ParticleGoal extends RoundPhysical implements Collidable {
  private ParticleGoalAction action;
  private final Color color = new Color(87, 107, 255);
  private final Color internalColor;

  public ParticleGoal(double x, double y, double rotation, double radius, ParticleGoalAction action, Color internal) {
    super(x, y, rotation, radius);
    this.action = action;
    this.internalColor = internal;
  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(color);
    g.drawOval((int)getX(), (int)getY(), (int)getWidth(), (int)getHeight());
    g.setColor(internalColor);
    g.drawOval((int)getX() + 1, (int)getY() + 1, (int)getWidth() - 1, (int)getHeight() - 1);
  }

  @Override
  public boolean isCollision(Particle particle) {
    return CollisionUtil.areColliding(this, particle.getX(), particle.getY());
  }

  @Override
  public void actCollision(Particle particle, Model model) {
    action.hit(particle);
  }
}
