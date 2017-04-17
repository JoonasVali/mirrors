package ee.joonasvali;

import ee.joonasvali.graphics.DefaultRenderer;
import ee.joonasvali.graphics.Renderer;
import ee.joonasvali.scene.Environment;
import ee.joonasvali.scene.EnvironmentBuilder;

import java.awt.Graphics2D;


/**
 * @author Joonas Vali April 2016
 */
public class DemoEnvironmentController implements EnvironmentController {
  private final EnvironmentBuilder environmentBuilder;
  private final Renderer renderer;
  private volatile long lastRender;
  private volatile Environment environment;

  public DemoEnvironmentController(EnvironmentBuilder environment) {
    this.environmentBuilder = environment;
    this.renderer = new DefaultRenderer();
  }

  public void init() {
    this.environment = environmentBuilder.getEnvironment();
  }

  public void render(Graphics2D g) {
    renderer.render(environment, g);
  }

  public void keyPressed(int key, char c) {

  }

  public void keyReleased(int key, char c) {
    /**
     * Replays scene when space is pressed.
     */
//    if(key == Input.KEY_SPACE){
//      init();
//    }
  }

  @Override
  public void nextStep() {
    long time = System.currentTimeMillis();
    environment.act(time - lastRender);
    lastRender = time;
  }

  @Override
  public Environment getEnvironment() {
    return environment;
  }
}
