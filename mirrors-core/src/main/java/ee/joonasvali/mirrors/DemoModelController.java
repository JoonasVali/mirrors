package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.graphics.DefaultRenderer;
import ee.joonasvali.mirrors.graphics.Renderer;
import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ModelBuilder;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;


/**
 * @author Joonas Vali April 2016
 */
public class DemoModelController  implements ModelController {
  private final ModelBuilder modelBuilder;
  private final Renderer renderer;
  private volatile long lastRender;
  private volatile Model model;
  private final KeyListener keyListener;
  private final Object LOCK = new Object() {
    @Override
    public String toString() {
      return "model.lock";
    }
  };

  public DemoModelController(ModelBuilder builder) {
    this.modelBuilder = builder;
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
    this.model = modelBuilder.buildModel();
    this.model.setLock(LOCK);
  }

  public void render(Graphics2D g) {
    renderer.render(model, g);
  }

  @Override
  public void nextStep() {
    long time = System.currentTimeMillis();
    model.act(time - lastRender);
    lastRender = time;
  }

  @Override
  public Model getModel() {
    return model;
  }

  @Override
  public KeyListener getKeyListener() {
    return keyListener;
  }
}
