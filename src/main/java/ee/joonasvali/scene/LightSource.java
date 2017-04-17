package ee.joonasvali.scene;

import java.awt.Color;
import java.awt.Graphics2D;

public class LightSource extends RoundPhysical implements Activatable {

  private final Environment env;
  private LightEmitterProperties properties;
  private boolean hasActivated;
  private Itensity0Action onIntensity0;

  public LightSource(double x, double y, LightEmitterProperties properties, Environment environment) {
    super(x, y, 0, 10);
    this.env = environment;
    onIntensity0 = env::remove;
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

    for (double i = 0; i < 360; i += density) {
      Light light = new Light(getCenterX(), getCenterY(), i, velocity, 100, 0.1, onIntensity0);
      env.addObject(light);
    }

  }
}
