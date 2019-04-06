package ee.joonasvali.mirrors.command;

import ee.joonasvali.mirrors.DemoModelController;
import ee.joonasvali.mirrors.ModelController;
import ee.joonasvali.mirrors.EvolutionController;
import ee.joonasvali.mirrors.EvolutionProperties;
import ee.joonasvali.mirrors.EvolutionPropertyLoader;
import ee.joonasvali.mirrors.scene.Constants;
import ee.joonasvali.mirrors.scene.ModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.GeneticModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.impl.GeneratorGenomeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.awt.*;
import java.nio.file.Paths;
import java.util.Random;

public class RunRandomScene {
  private final static Logger log = LoggerFactory.getLogger(RunRandomScene.class);

  public static ModelController runRandom(String[] args) {
    if (GraphicsEnvironment.isHeadless()) {
      log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
      System.exit(-1);
    }

    Random random = new MersenneTwisterRNG();
    EvolutionPropertyLoader propertyLoader = new EvolutionPropertyLoader();
    EvolutionProperties evolutionProperties = propertyLoader.loadProperties(
        // Load evolution.properties from current dir.
        Paths.get(EvolutionController.EVOLUTION_PROPERTIES_FILE_NAME)
    );
    GeneFactory geneFactory = new GeneFactory(evolutionProperties, random);
    ModelBuilder builder = new GeneticModelBuilder(
        new GeneratorGenomeProvider(geneFactory,
            Constants.DIMENSION_X, Constants.DIMENSION_Y,
            evolutionProperties.isTopProducerEnabled(),
            evolutionProperties.isMiddleProducerEnabled(),
            evolutionProperties.isBottomProducerEnabled()
        )
    );
    return new DemoModelController(builder);
  }
}
