package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.Light;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.genetic.impl.ReflectorGene;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * @author Joonas Vali April 2017
 */
public class GraphicalMirrorTest {
  @Test
  public void reflectorTest() throws InvocationTargetException, InterruptedException {
    EnvironmentBuilder builder = () -> {
      Environment environment = new Environment();

      ReflectorGene gene = new ReflectorGene(350, 250, 50, 50);
      List<LinePhysical> list = gene.createLinePhysicals(environment);
      list.forEach(environment::addObject);

      environment.addObject(new Light(300, 200, 125, 1, 100, 0.1));
      environment.addObject(new Light(150, 150, 125, 1, 100, 0.1));

      environment.addObject(new Light(500, 100, 225, 1, 100, 0.1));
      environment.addObject(new Light(510, 100, 224, 1, 100, 0.1));

      environment.addObject(new Light(480, 230, 258, 1, 100, 0.1));
      environment.addObject(new Light(480, 230, 230, 1, 100, 0.1));

      environment.addObject(new Light(350, 350, 10, 1, 100, 0.1));
      environment.addObject(new Light(440, 350, 310, 1, 100, 0.1));

      return environment;
    };
    EnvironmentController controller = new DemoEnvironmentController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }

  @Test
  public void line1Test() throws InvocationTargetException, InterruptedException {
    EnvironmentBuilder builder = () -> {
      Environment environment = new Environment();

      environment.addObject(new LinePhysical(350, 250, 400, 290));

      environment.addObject(new Light(300, 150, 150, 1, 100, 0.1));
      environment.addObject(new Light(150, 150, 118, 1, 100, 0.1));

      environment.addObject(new Light(510, 100, 225, 1, 100, 0.1));
      environment.addObject(new Light(520, 100, 224, 1, 100, 0.1));

      environment.addObject(new Light(480, 230, 258, 1, 100, 0.1));
      environment.addObject(new Light(480, 230, 230, 1, 100, 0.1));

      environment.addObject(new Light(350, 350, 10, 1, 100, 0.1));
      environment.addObject(new Light(400, 350, 0, 1, 100, 0.1));

      return environment;
    };
    EnvironmentController controller = new DemoEnvironmentController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }

  @Test
  public void line2Test() throws InvocationTargetException, InterruptedException {
    EnvironmentBuilder builder = () -> {
      Environment environment = new Environment();

      environment.addObject(new LinePhysical(450, 250, 300, 290));

      environment.addObject(new Light(300, 150, 150, 1, 100, 0.1));
      environment.addObject(new Light(150, 150, 118, 1, 100, 0.1));

      environment.addObject(new Light(510, 100, 225, 1, 100, 0.1));
      environment.addObject(new Light(520, 100, 224, 1, 100, 0.1));

      environment.addObject(new Light(480, 250, 258, 1, 100, 0.1));
      environment.addObject(new Light(480, 210, 230, 1, 100, 0.1));

      environment.addObject(new Light(350, 350, 10, 1, 100, 0.1));
      environment.addObject(new Light(400, 350,  0, 1, 100, 0.1));

      return environment;
    };
    EnvironmentController controller = new DemoEnvironmentController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }

  @Test
  public void line3Test() throws InvocationTargetException, InterruptedException {
    EnvironmentBuilder builder = () -> {
      Environment environment = new Environment();

      environment.addObject(new LinePhysical(450, 250, 300, 95));

      environment.addObject(new Light(300, 150, 48, 1, 100, 0.1));
      environment.addObject(new Light(150, 150, 118, 1, 100, 0.1));

      environment.addObject(new Light(510, 100, 265, 1, 100, 0.1));
      environment.addObject(new Light(520, 100, 224, 1, 100, 0.1));

      environment.addObject(new Light(480, 250, 258, 1, 100, 0.1));
      environment.addObject(new Light(480, 210, 230, 1, 100, 0.1));

      environment.addObject(new Light(350, 350, 10, 1, 100, 0.1));
      environment.addObject(new Light(400, 350,  0, 1, 100, 0.1));

      return environment;
    };
    EnvironmentController controller = new DemoEnvironmentController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }

  @Test
  public void line4Test() throws InvocationTargetException, InterruptedException {
    EnvironmentBuilder builder = () -> {
      Environment environment = new Environment();

      environment.addObject(new LinePhysical(300, 250, 470, 95));

      environment.addObject(new Light(300, 150, 90, 1, 100, 0.1));
      environment.addObject(new Light(150, 150, 118, 1, 100, 0.1));

      environment.addObject(new Light(510, 100, 300, 1, 100, 0.1));
      environment.addObject(new Light(520, 100, 324, 1, 100, 0.1));

      environment.addObject(new Light(480, 250, 358, 1, 100, 0.1));
      environment.addObject(new Light(480, 210, 330, 1, 100, 0.1));

      environment.addObject(new Light(350, 350, 10, 1, 100, 0.1));
      environment.addObject(new Light(400, 350,  0, 1, 100, 0.1));

      return environment;
    };
    EnvironmentController controller = new DemoEnvironmentController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }
}
