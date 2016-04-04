package ee.joonasvali.scene;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * Created with IntelliJ IDEA.
 * User: Joss
 * Date: 3/23/14
 * Time: 3:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class LightSource extends RoundPhysical implements Activatable, LightEmitter {

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
  public void render(Graphics g) {
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

  @Override
  public void emit(LightEmitterProperties properties) {
    double density = properties.getDensity();
    double velocity = properties.getVelocity();

    for (double i = 0; i < 360; i += density) {
      Light light = new Light(getCenterX(), getCenterY(), i, velocity, 100, 0.1, onIntensity0);
      env.addObject(light);
    }

  }
}
