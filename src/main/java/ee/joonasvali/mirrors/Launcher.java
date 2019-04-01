package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.GeneticEnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.impl.GeneratorGenepoolProvider;
import ee.joonasvali.mirrors.scene.genetic.impl.LoaderGenepoolProvider;
import ee.joonasvali.mirrors.scene.genetic.util.SerializationUtil;
import ee.joonasvali.mirrors.util.SceneFileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.MersenneTwisterRNG;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;
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
      EvolutionPropertyLoader propertyLoader = new EvolutionPropertyLoader();
      GeneFactory geneFactory = new GeneFactory(propertyLoader.createDefaultEvolutionProperties(), random);
      EnvironmentBuilder builder = new GeneticEnvironmentBuilder(
          new GeneratorGenepoolProvider(geneFactory, 750, 550)
      );
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

    if (arg.equals("create-evolution")) {
      log.info("Creating evolution directory.");
      return createEvolutionDirectory(args);
    }

    log.error("Arguments didn't match anything we know how to parse.");
    return null;
  }

  private static EnvironmentController createEvolutionDirectory(String[] args) {
    if (args.length < 2) {
      log.error("expected parameter to be the file pointing to target evolution directory");
    }

    String evolutionDirectoryPath = args[1];
    Path evolutionDirectory = Paths.get(evolutionDirectoryPath);

    if (!Files.exists(evolutionDirectory)) {
      // If no directory is present then create it.
      try {
        Files.createDirectories(evolutionDirectory);
      } catch (IOException e) {
        log.error("Unable to create evolution directory "  + evolutionDirectory, e);
        throw new RuntimeException(e);
      }
    } else {
      // If existing empty directory is present then accept it as evolution directory.
      try {
        if (Files.list(evolutionDirectory).count() != 0) {
          throw new IllegalArgumentException("Directory already exists and is not empty.");
        }
      } catch (IOException e) {
        log.error("Unable to read contents of evolution directory " + evolutionDirectory, e);
      }
    }

    Path runEvolutionFile = evolutionDirectory.resolve("run.cmd");

    // This is an assumption that the script is launched from HOME/bin dir.
    Path mirrorsHome;
    try {
      mirrorsHome = Paths.get("..").toRealPath();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    try {
      String content = "" +
          "@echo off\n" +
          "SET MIRRORS_HOME=" + mirrorsHome.toString() + "\n" +
          "java -jar -Xmx2048M %MIRRORS_HOME%/lib/mirrors.jar evolution evolution.properties";

      Files.write(runEvolutionFile, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW);
    } catch (IOException e) {
      throw new RuntimeException("Unable to create run file." + runEvolutionFile, e);
    }

    try {
      Path sourcePropertyFile = mirrorsHome.resolve("config").resolve("evolution.win.properties");
      Path evolutionPropertyFile = evolutionDirectory.resolve(EvolutionController.EVOLUTION_PROPERTIES_FILE_NAME);
      Files.copy(sourcePropertyFile, evolutionPropertyFile);
    } catch (IOException e) {
      log.error("Unable to create evolution directory.", e);
      throw new RuntimeException(e);
    }

    return null;
  }

  private static EnvironmentController evolution(String[] args) {
    if (args.length < 2) {
      log.error("expected second parameter to be the file pointing to evolution.properties");
      return null;
    }

    // These are the properties copied to evolution directory when new evolution is started.
    Path sourceProperties = Paths.get(args[1]);
    // Assume current dir is evolution directory.
    Path evolutionDirectory = Paths.get(System.getProperty("user.dir"));
    Path evolutionFile = evolutionDirectory.resolve(EvolutionController.POPULATIONS_FILE_NAME);
    Path evolutionPropertyFile = evolutionDirectory.resolve(EvolutionController.EVOLUTION_PROPERTIES_FILE_NAME);
    Collection<Genepool> seedPopulation = Collections.emptyList();

    if (!Files.exists(evolutionDirectory)) {
      try {
        Files.createDirectory(evolutionDirectory);
      } catch (IOException e) {
        log.error("Unable to create evolution directory.", e);
        throw new RuntimeException(e);
      }
    }

    if (!Files.exists(evolutionPropertyFile)) {
      try {
        Files.copy(sourceProperties, evolutionPropertyFile);
      } catch (IOException e) {
        log.error("Unable to create evolution directory.", e);
        throw new RuntimeException(e);
      }
    }

    if (Files.exists(evolutionFile)) {
      log.info("Loaded existing population from evolution file: " + evolutionFile);
      try {
        seedPopulation = SerializationUtil.deserializePopulation(evolutionFile);
      } catch (IOException e) {
        log.error("Unable to read evolution file.");
        throw new RuntimeException(e);
      }
    }

    EvolutionController controller = new EvolutionController(evolutionDirectory);
    Optional<Genepool> winner = controller.runEvolution(seedPopulation);
    if (winner.isPresent()) {
      EnvironmentBuilder builder = new GeneticEnvironmentBuilder(new LoaderGenepoolProvider(winner.get()));
      return new DemoEnvironmentController(builder);
    }
    return null;
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
