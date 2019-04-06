package ee.joonasvali.mirrors.scene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ParticleSource extends RoundPhysical implements Activatable {
  private final ParticleGroup particleGroup;
  private final Model env;
  private final double density;
  private final double velocity;
  private boolean hasActivated;

  public ParticleSource(
      double x, double y, double density, double velocity, Model model, ParticleGroup particleGroup
  ) {
    super(x, y, 0, 10);
    this.density = density;
    this.velocity = velocity;
    this.particleGroup = particleGroup;
    this.env = model;

  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(Color.red);
    g.drawOval((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
  }

  @Override
  public void activate() {
    if (!hasActivated) {
      emit();
      hasActivated = true;
    }
  }

  public void emit() {
    List<Particle> particles = new ArrayList<>();
    for (double i = 0; i < 360; i += density) {
      Particle particle = new Particle(getCenterX(), getCenterY(), i, velocity, 100, 0.1, particleGroup);
      particles.add(particle);
    }
    env.addParticles(particles);

  }
}
