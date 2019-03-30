package ee.joonasvali.mirrors.graphical;

import ee.joonasvali.mirrors.DemoEnvironmentController;
import ee.joonasvali.mirrors.EnvironmentController;
import ee.joonasvali.mirrors.WindowController;
import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.Light;
import ee.joonasvali.mirrors.scene.LightGroup;
import ee.joonasvali.mirrors.scene.LineReflector;
import org.junit.Test;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

/**
 * @author Joonas Vali April 2017
 */
public class GraphicalMirrorTest {
  private final LightGroup group = new LightGroup(1, new Color(255, 255, 255));

  @Test
  public void line1Test() throws InvocationTargetException, InterruptedException {
    EnvironmentBuilder builder = () -> {
      Environment environment = new Environment();

      environment.addObject(new LineReflector(350, 250, 400, 290));

      environment.addObject(new Light(300, 150, 150, 1, 100, 0.1, group));
      environment.addObject(new Light(150, 150, 118, 1, 100, 0.1, group));

      environment.addObject(new Light(510, 100, 225, 1, 100, 0.1, group));
      environment.addObject(new Light(520, 100, 224, 1, 100, 0.1, group));

      environment.addObject(new Light(480, 230, 258, 1, 100, 0.1, group));
      environment.addObject(new Light(480, 230, 230, 1, 100, 0.1, group));

      environment.addObject(new Light(350, 350, 10, 1, 100, 0.1, group));
      environment.addObject(new Light(400, 350, 0, 1, 100, 0.1, group));

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

      environment.addObject(new LineReflector(450, 250, 300, 290));

      environment.addObject(new Light(300, 150, 150, 1, 100, 0.1, group));
      environment.addObject(new Light(150, 150, 118, 1, 100, 0.1, group));

      environment.addObject(new Light(510, 100, 225, 1, 100, 0.1, group));
      environment.addObject(new Light(520, 100, 224, 1, 100, 0.1, group));

      environment.addObject(new Light(480, 250, 258, 1, 100, 0.1, group));
      environment.addObject(new Light(480, 210, 230, 1, 100, 0.1, group));

      environment.addObject(new Light(350, 350, 10, 1, 100, 0.1, group));
      environment.addObject(new Light(400, 350,  0, 1, 100, 0.1, group));

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

      environment.addObject(new LineReflector(450, 250, 300, 95));

      environment.addObject(new Light(300, 150, 48, 1, 100, 0.1, group));
      environment.addObject(new Light(150, 150, 118, 1, 100, 0.1, group));

      environment.addObject(new Light(510, 100, 265, 1, 100, 0.1, group));
      environment.addObject(new Light(520, 100, 224, 1, 100, 0.1, group));

      environment.addObject(new Light(480, 250, 258, 1, 100, 0.1, group));
      environment.addObject(new Light(480, 210, 230, 1, 100, 0.1, group));

      environment.addObject(new Light(350, 350, 10, 1, 100, 0.1, group));
      environment.addObject(new Light(400, 350,  0, 1, 100, 0.1, group));

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

      environment.addObject(new LineReflector(300, 250, 470, 95));

      environment.addObject(new Light(300, 150, 90, 1, 100, 0.1, group));
      environment.addObject(new Light(150, 150, 118, 1, 100, 0.1, group));

      environment.addObject(new Light(510, 100, 300, 1, 100, 0.1, group));
      environment.addObject(new Light(520, 100, 324, 1, 100, 0.1, group));

      environment.addObject(new Light(480, 250, 358, 1, 100, 0.1, group));
      environment.addObject(new Light(480, 210, 330, 1, 100, 0.1, group));

      environment.addObject(new Light(350, 350, 10, 1, 100, 0.1, group));
      environment.addObject(new Light(400, 350,  0, 1, 100, 0.1, group));

      return environment;
    };
    EnvironmentController controller = new DemoEnvironmentController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }

  @Test
  public void lineExcludedGroupTest() throws InvocationTargetException, InterruptedException {
    EnvironmentBuilder builder = () -> {
      Environment environment = new Environment();

      LightGroup group2 = new LightGroup(2, new Color(230, 46, 56));
      environment.addObject(new LineReflector(300, 250, 470, 95, new HashSet<>(Collections.singletonList(group.getId()))));

      environment.addObject(new Light(300, 150, 90, 1, 100, 0.1, group2));
      environment.addObject(new Light(150, 150, 118, 1, 100, 0.1, group2));

      environment.addObject(new Light(510, 100, 250, 1, 100, 0.1, group2));
      environment.addObject(new Light(520, 100, 250, 1, 100, 0.1, group2));

      environment.addObject(new Light(480, 250, 358, 1, 100, 0.1, group));
      environment.addObject(new Light(480, 210, 330, 1, 100, 0.1, group));

      environment.addObject(new Light(350, 350, 10, 1, 100, 0.1, group));
      environment.addObject(new Light(400, 350,  0, 1, 100, 0.1, group));

      return environment;
    };
    EnvironmentController controller = new DemoEnvironmentController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }
}
