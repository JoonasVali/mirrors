package ee.joonasvali.mirrors.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Model {
  private final List<Physical> objects = new CopyOnWriteArrayList<>();
  private final List<LinePhysical> lines = new CopyOnWriteArrayList<>();
  private volatile List<Particle> particles = new CopyOnWriteArrayList<>();
  private double score = 0;
  private Object lock;
  private final int screenWidth = Constants.DIMENSION_X;
  private final int screenHeight = Constants.DIMENSION_Y;

  public double getScore() {
    return score;
  }

  public void addScore(double score) {
    this.score += score;
    if (this.score < 0) {
      this.score = 0;
    }
  }

  public void addObject(LinePhysical line) {
    this.lines.add(line);
  }

  public void addObject(Physical physical) {
    if(physical instanceof Particle) {
      particles.add((Particle)physical);
      return;
    }
    this.objects.add(physical);
  }

  public void actUntilNoParticlesLeft() {
    do {
      act(1);
    } while (hasParticlesRemaining());
  }

  public void act(long delta) {
    // check collisions for particle and objects
    for(LinePhysical line : lines) {
      for(Particle particle : particles){
        if(line.isCollision(particle)) {
          line.actCollision(particle, this);
        }
      }
    }

    for(Physical object : objects) {
      if(object instanceof Collidable) {
        Collidable collidable = (Collidable) object;
        for(Particle particle : particles){
          if(collidable.isCollision(particle)) {
            collidable.actCollision(particle, this);
          }
        }
      }
    }

    for (Physical object : objects) {
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

  public List<Physical> getObjects() {
    return objects;
  }

  public List<Particle> getParticles() {
    return particles;
  }

  public void remove(Physical object) {
    if(object instanceof Particle){
      particles.remove(object);
      return;
    }
    objects.remove(object);
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

  public List<LinePhysical> getLines() {
    return lines;
  }

  public void addParticles(List<Particle> particles) {
    this.particles.addAll(particles);
  }
}
