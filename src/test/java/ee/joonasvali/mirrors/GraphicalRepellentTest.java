package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.Light;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.impl.RepellentGene;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

public class GraphicalRepellentTest {
  @Test
  public void repellentTest() throws InvocationTargetException, InterruptedException {
    EnvironmentBuilder builder = () -> {
      Environment environment = new Environment();

      RepellentGene gene = new RepellentGene(350, 250, 50, 0.01f);
      List<Physical> list = gene.createPhysicals(environment);
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
}
