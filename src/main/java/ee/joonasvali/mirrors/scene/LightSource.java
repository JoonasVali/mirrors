package ee.joonasvali.mirrors.scene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LightSource extends RoundPhysical implements Activatable {
  private final LightGroup lightGroup;
  private final Environment env;
  private final double density;
  private final double velocity;
  private boolean hasActivated;

  public LightSource(
      double x, double y, double density, double velocity, Environment environment, LightGroup lightGroup
  ) {
    super(x, y, 0, 10);
    this.density = density;
    this.velocity = velocity;
    this.lightGroup = lightGroup;
    this.env = environment;

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
    List<Light> particles = new ArrayList<>();
    for (double i = 0; i < 360; i += density) {
      Light light = new Light(getCenterX(), getCenterY(), i, velocity, 100, 0.1, lightGroup);
      particles.add(light);
    }
    env.addParticles(particles);

  }
}
