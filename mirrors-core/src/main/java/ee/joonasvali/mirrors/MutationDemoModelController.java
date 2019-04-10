package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.graphics.DefaultRenderer;
import ee.joonasvali.mirrors.graphics.Renderer;
import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.GeneticModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MutationDemoModelController implements ModelController {
  private volatile Genome genome;
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
  private final EvolutionaryOperator<Genome> operator;
  private final Random random;

  public MutationDemoModelController(Genome genome, EvolutionaryOperator<Genome> operator, Random random) {
    this.random = random;
    this.operator = operator;
    this.genome = genome;
    this.renderer = new DefaultRenderer();
    keyListener = createKeyListener();
  }

  private KeyListener createKeyListener() {
    return new KeyAdapter() {
      @Override
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
          mutate();
          init();
        }
      }
    };
  }

  public void init() {
    ModelBuilder modelBuilder = new GeneticModelBuilder(genome);
    this.model = modelBuilder.buildModel();
    this.model.setLock(LOCK);
  }

  public void mutate() {
    List<Genome> result = operator.apply(Collections.singletonList(genome), random);
    genome = result.get(0);
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

