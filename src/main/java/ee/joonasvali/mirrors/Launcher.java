package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.GeneticEnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.impl.GeneratorGenepoolProvider;
import ee.joonasvali.mirrors.scene.genetic.impl.LoaderGenepoolProvider;
import ee.joonasvali.mirrors.util.SceneFileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.Properties;
import java.util.Random;

/**
 * @author Joonas Vali April 2016
 */
public class Launcher {
  private static final Logger log = LoggerFactory.getLogger(Launcher.class);

  public static void main(String[] args) throws InvocationTargetException, InterruptedException {
    EnvironmentController env = getEnvironment(args);
    WindowController controller = new WindowController(env);
    controller.launch();
  }

  private static EnvironmentController getEnvironment(String[] args) {
    if (args.length == 0) {
      if (GraphicsEnvironment.isHeadless()) {
        log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
        System.exit(-1);
      }

      log.info("No program arguments, launching random demo scene.");
      Random random = new MersenneTwisterRNG();
      GeneFactory geneFactory = new GeneFactory(createDefaultEvolutionProperties(), random);
      EnvironmentBuilder builder = new GeneticEnvironmentBuilder(new GeneratorGenepoolProvider(geneFactory, 750, 550));
      return new DemoEnvironmentController(builder);
    }

    String arg = args[0];
    if (arg.equals("load")) {
      log.info("Launching GUI file loader.");
      return chooseFileWithGUI();
    }

    if (arg.equals("evolution")) {
      log.info("Launching Evolution mode.");
      return evolution(args);
    }

    log.error("Arguments didn't match anything we know how to parse.");
    return null; // satisfy compiler
  }

  private static EvolutionProperties createDefaultEvolutionProperties() {
    return new EvolutionProperties(
        DefaultEvolutionPropertyValues.ELITES,
        DefaultEvolutionPropertyValues.CONCURRENT,
        DefaultEvolutionPropertyValues.TARGET_FITNESS,
        DefaultEvolutionPropertyValues.KEEP_MACHINE_ALIVE,
        new File("../../saved"),
        DefaultEvolutionPropertyValues.GENE_MUTATION_RATE,
        DefaultEvolutionPropertyValues.GENE_ADDITION_RATE,
        DefaultEvolutionPropertyValues.GENE_DELETION_RATE,
        DefaultEvolutionPropertyValues.REFLECTORS_ENABLED,
        DefaultEvolutionPropertyValues.BENDERS_ENABLED,
        DefaultEvolutionPropertyValues.ACCELERATORS_ENABLED,
        DefaultEvolutionPropertyValues.REPELLENTS_ENABLED
    );
  }

  private static EnvironmentController evolution(String[] args) {
    if (args.length < 2) {
      log.error("expected second argument to be the file pointing to evolution.properties");
      return null;
    }
    EvolutionProperties properties = loadProperties(args[1]);
    if (properties == null) {
      log.error("properties == null");
      return null;
    }

    EvolutionController controller = new EvolutionController(properties);
    Optional<Genepool> winner = controller.runEvolution();
    if (winner.isPresent()) {
      EnvironmentBuilder builder = new GeneticEnvironmentBuilder(new LoaderGenepoolProvider(winner.get()));
      return new DemoEnvironmentController(builder);
    }
    return null;
  }

  private static EvolutionProperties loadProperties(String name) {
    EvolutionProperties properties = null;
    try {
      File userDir = new File(System.getProperty("user.dir"));
      File propertiesFile = new File(userDir, name).getCanonicalFile();
      log.info("Properties read from: " + propertiesFile);
      Properties props = new Properties();
      try (InputStream stream = new FileInputStream(propertiesFile)) {
        props.load(stream);
      }

      File savedDir = new File(userDir, props.getProperty("output.dir", "../../saved")).getCanonicalFile();

      properties = new EvolutionProperties(
          Integer.parseInt(props.getProperty("elites", String.valueOf(DefaultEvolutionPropertyValues.ELITES))),
          Integer.parseInt(props.getProperty("concurrent", String.valueOf(DefaultEvolutionPropertyValues.CONCURRENT))),
          Integer.parseInt(props.getProperty("target.fitness", String.valueOf(DefaultEvolutionPropertyValues.TARGET_FITNESS))),
          Boolean.parseBoolean(props.getProperty("keep.machine.alive", String.valueOf(DefaultEvolutionPropertyValues.KEEP_MACHINE_ALIVE))),
          savedDir,
          Double.parseDouble(props.getProperty("gene.mutation.rate", String.valueOf(DefaultEvolutionPropertyValues.GENE_MUTATION_RATE))),
          Double.parseDouble(props.getProperty("gene.deletion.rate", String.valueOf(DefaultEvolutionPropertyValues.GENE_DELETION_RATE))),
          Double.parseDouble(props.getProperty("gene.addition.rate", String.valueOf(DefaultEvolutionPropertyValues.GENE_ADDITION_RATE))),
          Boolean.parseBoolean(props.getProperty("reflectors.enabled", String.valueOf(DefaultEvolutionPropertyValues.REFLECTORS_ENABLED))),
          Boolean.parseBoolean(props.getProperty("benders.enabled", String.valueOf(DefaultEvolutionPropertyValues.BENDERS_ENABLED))),
          Boolean.parseBoolean(props.getProperty("accelerators.enabled", String.valueOf(DefaultEvolutionPropertyValues.ACCELERATORS_ENABLED))),
          Boolean.parseBoolean(props.getProperty("repellents.enabled", String.valueOf(DefaultEvolutionPropertyValues.REPELLENTS_ENABLED)))
      );
    } catch (IOException e) {
      log.error("Can't read properties " + name, e);
      System.exit(-1);
    }

    return properties;
  }

  private static DemoEnvironmentController chooseFileWithGUI() {
    if (GraphicsEnvironment.isHeadless()) {
      log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
      System.exit(-1);
    }

    SceneFileChooser fileChooser = new SceneFileChooser();
    File file = fileChooser.selectFile(System.getProperty("user.dir"));
    if (file == null) {
      log.error("No file selected");
      System.exit(-1);
    } else {
      EnvironmentBuilder builder = new GeneticEnvironmentBuilder(new LoaderGenepoolProvider(file));
      return new DemoEnvironmentController(builder);
    }
    return null;
  }

}
