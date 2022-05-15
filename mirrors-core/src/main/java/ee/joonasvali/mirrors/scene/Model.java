package ee.joonasvali.mirrors.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Model {
  // We assign small penalty for every object. This leads evolution to remove unnecessary genes over time.
  public static final float OBJECT_COUNT_PENALTY = 1f;
  private final List<Collidable> objects = new CopyOnWriteArrayList<>();
  private volatile List<Particle> particles = new CopyOnWriteArrayList<>();
  private double score = 0;
  private Object lock;
  private final int screenWidth = Constants.DIMENSION_X;
  private final int screenHeight = Constants.DIMENSION_Y;

  public double getScore() {
    return Math.max(0, score);
  }

  public void addScore(double score) {
    this.score += score;
    if (this.score < 0) {
      this.score = 0;
    }
  }

  public void addObject(Particle particle) {
    particles.add(particle);
  }

  public void addObject(Collidable collidable) {
    this.score -= OBJECT_COUNT_PENALTY;
    this.objects.add(collidable);
  }

  public void actUntilNoParticlesLeft() {
    do {
      act(1);
    } while (hasParticlesRemaining());
  }

  public void act(long delta) {
    for (Collidable collidable : objects) {
      for (Particle particle : particles) {
        if (collidable.isCollision(particle)) {
          collidable.actCollision(particle, this);
        }
      }
    }

    for (Collidable object : objects) {
      if (object instanceof Activatable) {
        ((Activatable) object).activate();
      }
    }

    particles.forEach(Particle::activate);
    particles.removeIf(particle ->
        particle.getIntensity() <= 0 ||
            particle.getSpeed() <= 0 ||
            particle.getX() < 0 ||
            particle.getY() < 0 ||
            particle.getX() > screenWidth ||
            particle.getY() > screenHeight);
  }

  public List<Collidable> getObjects() {
    return objects;
  }

  public List<Particle> getParticles() {
    return particles;
  }

  public void removeParticle(Particle particle) {
    particles.remove(particle);
  }

  public boolean hasParticlesRemaining() {
    return particles.size() > 0;
  }

  public Object getLock() {
    return lock;
  }

  public void setLock(Object lock) {
    this.lock = lock;
  }

  public void addParticles(List<Particle> particles) {
    this.particles.addAll(particles);
  }
}
