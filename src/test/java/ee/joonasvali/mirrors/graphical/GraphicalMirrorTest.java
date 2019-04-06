package ee.joonasvali.mirrors.graphical;

import ee.joonasvali.mirrors.DemoModelController;
import ee.joonasvali.mirrors.ModelController;
import ee.joonasvali.mirrors.WindowController;
import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ModelBuilder;
import ee.joonasvali.mirrors.scene.Particle;
import ee.joonasvali.mirrors.scene.ParticleGroup;
import ee.joonasvali.mirrors.scene.LineReflector;
import org.junit.Test;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Joonas Vali April 2017
 */
public class GraphicalMirrorTest {
  private final ParticleGroup group = new ParticleGroup(1, new Color(255, 255, 255));

  @Test
  public void line1Test() throws InvocationTargetException, InterruptedException {
    ModelBuilder builder = () -> {
      Model model = new Model();

      model.addObject(new LineReflector(350, 250, 400, 290));

      model.addObject(new Particle(300, 150, 150, 1, 100, 0.1, group));
      model.addObject(new Particle(150, 150, 118, 1, 100, 0.1, group));

      model.addObject(new Particle(510, 100, 225, 1, 100, 0.1, group));
      model.addObject(new Particle(520, 100, 224, 1, 100, 0.1, group));

      model.addObject(new Particle(480, 230, 258, 1, 100, 0.1, group));
      model.addObject(new Particle(480, 230, 230, 1, 100, 0.1, group));

      model.addObject(new Particle(350, 350, 10, 1, 100, 0.1, group));
      model.addObject(new Particle(400, 350, 0, 1, 100, 0.1, group));

      return model;
    };
    ModelController controller = new DemoModelController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }

  @Test
  public void line2Test() throws InvocationTargetException, InterruptedException {
    ModelBuilder builder = () -> {
      Model model = new Model();

      model.addObject(new LineReflector(450, 250, 300, 290));

      model.addObject(new Particle(300, 150, 150, 1, 100, 0.1, group));
      model.addObject(new Particle(150, 150, 118, 1, 100, 0.1, group));

      model.addObject(new Particle(510, 100, 225, 1, 100, 0.1, group));
      model.addObject(new Particle(520, 100, 224, 1, 100, 0.1, group));

      model.addObject(new Particle(480, 250, 258, 1, 100, 0.1, group));
      model.addObject(new Particle(480, 210, 230, 1, 100, 0.1, group));

      model.addObject(new Particle(350, 350, 10, 1, 100, 0.1, group));
      model.addObject(new Particle(400, 350,  0, 1, 100, 0.1, group));

      return model;
    };
    ModelController controller = new DemoModelController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }

  @Test
  public void line3Test() throws InvocationTargetException, InterruptedException {
    ModelBuilder builder = () -> {
      Model model = new Model();

      model.addObject(new LineReflector(450, 250, 300, 95));

      model.addObject(new Particle(300, 150, 48, 1, 100, 0.1, group));
      model.addObject(new Particle(150, 150, 118, 1, 100, 0.1, group));

      model.addObject(new Particle(510, 100, 265, 1, 100, 0.1, group));
      model.addObject(new Particle(520, 100, 224, 1, 100, 0.1, group));

      model.addObject(new Particle(480, 250, 258, 1, 100, 0.1, group));
      model.addObject(new Particle(480, 210, 230, 1, 100, 0.1, group));

      model.addObject(new Particle(350, 350, 10, 1, 100, 0.1, group));
      model.addObject(new Particle(400, 350,  0, 1, 100, 0.1, group));

      return model;
    };
    ModelController controller = new DemoModelController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }

  @Test
  public void line4Test() throws InvocationTargetException, InterruptedException {
    ModelBuilder builder = () -> {
      Model model = new Model();

      model.addObject(new LineReflector(300, 250, 470, 95));

      model.addObject(new Particle(300, 150, 90, 1, 100, 0.1, group));
      model.addObject(new Particle(150, 150, 118, 1, 100, 0.1, group));

      model.addObject(new Particle(510, 100, 300, 1, 100, 0.1, group));
      model.addObject(new Particle(520, 100, 324, 1, 100, 0.1, group));

      model.addObject(new Particle(480, 250, 358, 1, 100, 0.1, group));
      model.addObject(new Particle(480, 210, 330, 1, 100, 0.1, group));

      model.addObject(new Particle(350, 350, 10, 1, 100, 0.1, group));
      model.addObject(new Particle(400, 350,  0, 1, 100, 0.1, group));

      return model;
    };
    ModelController controller = new DemoModelController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }

  @Test
  public void lineExcludedGroupTest() throws InvocationTargetException, InterruptedException {
    ModelBuilder builder = () -> {
      Model model = new Model();

      ParticleGroup group2 = new ParticleGroup(2, new Color(230, 46, 56));
      model.addObject(new LineReflector(300, 250, 470, 95, new HashSet<>(Collections.singletonList(group.getId()))));

      model.addObject(new Particle(300, 150, 90, 1, 100, 0.1, group2));
      model.addObject(new Particle(150, 150, 118, 1, 100, 0.1, group2));

      model.addObject(new Particle(510, 100, 250, 1, 100, 0.1, group2));
      model.addObject(new Particle(520, 100, 250, 1, 100, 0.1, group2));

      model.addObject(new Particle(480, 250, 358, 1, 100, 0.1, group));
      model.addObject(new Particle(480, 210, 330, 1, 100, 0.1, group));

      model.addObject(new Particle(350, 350, 10, 1, 100, 0.1, group));
      model.addObject(new Particle(400, 350,  0, 1, 100, 0.1, group));

      return model;
    };
    ModelController controller = new DemoModelController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }
}
