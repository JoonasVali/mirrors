package ee.joonasvali.mirrors.command;

import ee.joonasvali.mirrors.DemoEnvironmentController;
import ee.joonasvali.mirrors.EnvironmentController;
import ee.joonasvali.mirrors.EvolutionController;
import ee.joonasvali.mirrors.EvolutionProperties;
import ee.joonasvali.mirrors.EvolutionPropertyLoader;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.GeneticEnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.impl.GeneratorGenepoolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.awt.*;
import java.nio.file.Paths;
import java.util.Random;

public class RunRandomScene {
  private final static Logger log = LoggerFactory.getLogger(RunRandomScene.class);

  public static EnvironmentController runRandom(String[] args) {
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
    EnvironmentBuilder builder = new GeneticEnvironmentBuilder(
        new GeneratorGenepoolProvider(geneFactory, 750, 550)
    );
    return new DemoEnvironmentController(builder);
  }
}
