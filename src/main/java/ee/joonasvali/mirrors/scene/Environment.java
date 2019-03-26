package ee.joonasvali.mirrors.scene;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;


public class Environment {
  private final List<Physical> objects = new CopyOnWriteArrayList<>();
  private final List<LinePhysical> lines = new CopyOnWriteArrayList<>();
  private volatile List<Light> particles = new CopyOnWriteArrayList<>();
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
    if(physical instanceof Light) {
      particles.add((Light)physical);
      return;
    }
    this.objects.add(physical);
  }

  public void actUntilNoLightLeft() {
    do {
      act(1);
    } while (hasParticlesRemaining());
  }

  public void act(long delta) {
    // check collisions for light and objects
    for(LinePhysical line : lines) {
      for(Light light : particles){
        if(line.isCollision(light)) {
          line.actCollision(light, this);
        }
      }
    }

    for(Physical object : objects) {
      if(object instanceof Collidable) {
        Collidable collidable = (Collidable) object;
        for(Light light : particles){
          if(collidable.isCollision(light)) {
            collidable.actCollision(light, this);
          }
        }
      }
    }

    for (Physical object : objects) {
      if (object instanceof Activatable) {
        ((Activatable) object).activate();
      }
    }

    particles.forEach(Light::activate);
    particles.removeIf(light ->
        light.getIntensity() <= 0 ||
            light.getSpeed() <= 0 ||
            light.getX() < 0 ||
            light.getY() < 0 ||
            light.getX() > screenWidth ||
            light.getY() > screenHeight);
  }

  public List<Physical> getObjects() {
    return objects;
  }

  public List<Light> getParticles() {
    return particles;
  }

  public void remove(Physical object) {
    if(object instanceof Light){
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

  public void addParticles(List<Light> particles) {
    this.particles.addAll(particles);
  }
}
