package ee.joonasvali.mirrors.scene;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class LightSource extends RoundPhysical implements Activatable {
  private final LightGroup lightGroup;
  private final Environment env;
  private final LightEmitterProperties properties;
  private boolean hasActivated;

  public LightSource(
      double x, double y, LightEmitterProperties properties, Environment environment, LightGroup lightGroup
  ) {
    super(x, y, 0, 10);
    this.lightGroup = lightGroup;
    this.env = environment;
    this.properties = properties;
  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(Color.red);
    g.drawOval((int) getX(), (int) getY(), (int) getWidth(), (int) getHeight());
  }

  @Override
  public void activate() {
    if (!hasActivated) {
      emit(properties);
      hasActivated = true;
    }
  }

  public void emit(LightEmitterProperties properties) {
    double density = properties.getDensity();
    double velocity = properties.getVelocity();

    List<Light> particles = new ArrayList<>();
    for (double i = 0; i < 360; i += density) {
      Light light = new Light(getCenterX(), getCenterY(), i, velocity, 100, 0.1, lightGroup);
      particles.add(light);
    }
    env.addParticles(particles);

  }
}
