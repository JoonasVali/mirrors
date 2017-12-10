package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.graphics.DefaultRenderer;
import ee.joonasvali.mirrors.graphics.Renderer;
import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * @author Joonas Vali April 2016
 */
public class DemoEnvironmentController implements EnvironmentController {
  private final EnvironmentBuilder environmentBuilder;
  private final Renderer renderer;
  private volatile long lastRender;
  private volatile Environment environment;
  private final KeyListener keyListener;
  private final Object LOCK = new Object() {
    @Override
    public String toString() {
      return "environment.lock";
    }
  };

  public DemoEnvironmentController(EnvironmentBuilder environment) {
    this.environmentBuilder = environment;
    this.renderer = new DefaultRenderer();
    keyListener = createKeyListener();
  }

  private KeyListener createKeyListener() {
    return new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          init();
        }
      }
    };
  }

  public void init() {
    this.environment = environmentBuilder.getEnvironment();
    this.environment.setLock(LOCK);
  }

  public void render(Graphics2D g) {
    renderer.render(environment, g);
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

  @Override
  public KeyListener getKeyListener() {
    return keyListener;
  }
}
