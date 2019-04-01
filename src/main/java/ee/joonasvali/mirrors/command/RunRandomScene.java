package ee.joonasvali.mirrors.command;

import ee.joonasvali.mirrors.DemoEnvironmentController;
import ee.joonasvali.mirrors.EnvironmentController;
import ee.joonasvali.mirrors.EvolutionPropertyLoader;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.GeneticEnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.impl.GeneratorGenepoolProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.awt.*;
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
    GeneFactory geneFactory = new GeneFactory(propertyLoader.createDefaultEvolutionProperties(), random);
    EnvironmentBuilder builder = new GeneticEnvironmentBuilder(
        new GeneratorGenepoolProvider(geneFactory, 750, 550)
    );
    return new DemoEnvironmentController(builder);
  }
}
