package ee.joonasvali.mirrors.scene;

import ee.joonasvali.mirrors.graphics.Util;

import java.awt.*;

/**
 * @author Joonas Vali April 2017
 */
public abstract class LinePhysical implements Collidable {

  protected final double x;
  protected final double y;
  protected final double x2;
  protected final double y2;

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

  private boolean isAboutToCollide(Particle particle) {
    double particleX = particle.getX();
    double particleY = particle.getY();
    double speed = particle.getSpeed();
    double angle = particle.getAngle();
    double xVector = speed * Math.cos(Math.toRadians(angle - 90));
    double yVector = speed * Math.sin(Math.toRadians(angle - 90));
    double nextX = particleX + xVector;
    double nextY = particleY + yVector;
    return Util.isIntersecting(particleX, particleY, nextX, nextY, x, y, x2, y2);
  }


  @Override
  public boolean isCollision(Particle particle) {
    return isAboutToCollide(particle);
  }

  @Override
  public void actCollision(Particle particle, Model model) {
    double particleX = particle.getX();
    double particleY = particle.getY();

    double xVector = particle.getSpeed() * Math.cos(Math.toRadians(particle.getAngle() - 90));
    double yVector = particle.getSpeed() * Math.sin(Math.toRadians(particle.getAngle() - 90));
    double nextX = particleX + xVector;
    double nextY = particleY + yVector;

    Point intersection = Util.getLineIntersection(particleX, particleY, nextX, nextY, x, y, x2, y2);
    if (intersection != null) {
      runLightCollisionAction(particle, xVector, yVector);
    }

  }

  abstract void runLightCollisionAction(Particle particle, double particleXVector, double particleYVector);

}
