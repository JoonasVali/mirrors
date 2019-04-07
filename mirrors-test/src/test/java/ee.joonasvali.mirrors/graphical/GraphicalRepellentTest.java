package ee.joonasvali.mirrors.graphical;

import ee.joonasvali.mirrors.DemoModelController;
import ee.joonasvali.mirrors.ModelController;
import ee.joonasvali.mirrors.WindowController;
import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ModelBuilder;
import ee.joonasvali.mirrors.scene.Particle;
import ee.joonasvali.mirrors.scene.ParticleGroup;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.impl.RepellentGene;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

@Ignore
@Category(GraphicalTest.class)
public class GraphicalRepellentTest {
  private final ParticleGroup group = new ParticleGroup(1, new Color(255, 255, 255));

  @Test
  public void repellentTest() throws InvocationTargetException, InterruptedException {
    ModelBuilder builder = () -> {
      Model model = new Model();

      RepellentGene gene = new RepellentGene(350, 250, 50, 0.01f);
      List<Physical> list = gene.createPhysicals(model);
      list.forEach(model::addObject);

      model.addObject(new Particle(300, 200, 125, 1, 100, 0.1, group));
      model.addObject(new Particle(150, 150, 125, 1, 100, 0.1, group));

      model.addObject(new Particle(500, 100, 225, 1, 100, 0.1, group));
      model.addObject(new Particle(510, 100, 224, 1, 100, 0.1, group));

      model.addObject(new Particle(480, 230, 258, 1, 100, 0.1, group));
      model.addObject(new Particle(480, 230, 230, 1, 100, 0.1, group));

      model.addObject(new Particle(350, 350, 10, 1, 100, 0.1, group));
      model.addObject(new Particle(440, 350, 310, 1, 100, 0.1, group));

      return model;
    };
    ModelController controller = new DemoModelController(builder);
    WindowController windowController = new WindowController(controller);
    windowController.launch();
  }
}
