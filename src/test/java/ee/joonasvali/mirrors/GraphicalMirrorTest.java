package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.Light;
import ee.joonasvali.mirrors.scene.Reflector;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Joonas Vali April 2017
 */
public class GraphicalMirrorTest {
  @Test
  public void testSomething() throws InvocationTargetException, InterruptedException {
    EnvironmentBuilder builder = () -> {
      Environment environment = new Environment();
      environment.addObject(new Reflector(350, 250, 0 , 50, 50));
      environment.addObject(new Light(300, 200, 35, 1, 100, 0.1));
      environment.addObject(new Light(50, 50, 35, 1, 100, 0.1));
      return environment;
    };
    EnvironmentController controller = new DemoEnvironmentController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }
}
