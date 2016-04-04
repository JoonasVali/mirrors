package ee.joonasvali;

import ee.joonasvali.graphics.DefaultRenderer;
import ee.joonasvali.graphics.Renderer;
import ee.joonasvali.scene.Environment;
import ee.joonasvali.scene.EnvironmentBuilder;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;

/**
 * @author Joonas Vali April 2016
 */
public class DemoEnvironmentController implements EnvironmentController {
  private final EnvironmentBuilder environmentBuilder;
  private final Renderer renderer;
  private volatile Environment environment;

  public DemoEnvironmentController(EnvironmentBuilder environment) {
    this.environmentBuilder = environment;
    this.renderer = new DefaultRenderer();
  }

  public void init() {
    this.environment = environmentBuilder.getEnvironment();
  }

  /**
   * @see org.newdawn.slick.BasicGame#render(org.newdawn.slick.GameContainer, org.newdawn.slick.Graphics)
   */
  public void render(Graphics g) {
    renderer.render(environment, g);
  }

  public void update(int delta) {
    environment.act(delta);
  }

  public void keyPressed(int key, char c) {

  }

  public void keyReleased(int key, char c) {
    /**
     * Replays scene when space is pressed.
     */
    if(key == Input.KEY_SPACE){
      init();
    }
  }
}
