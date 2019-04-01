package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.command.CreateEvolutionGUI;
import ee.joonasvali.mirrors.command.EvolutionDirectoryCreator;
import ee.joonasvali.mirrors.command.GUIFileChooser;
import ee.joonasvali.mirrors.command.RunRandomScene;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.GeneticEnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.impl.LoaderGenepoolProvider;
import ee.joonasvali.mirrors.scene.genetic.util.SerializationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

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
      log.info("No program arguments, launching random demo scene.");
      return RunRandomScene.runRandom(args);
    }

    String arg = args[0];
    if (arg.equals("load")) {
      log.info("Launching GUI file loader.");
      return GUIFileChooser.chooseFileWithGUI(args);
    }

    if (arg.equals("evolution")) {
      log.info("Launching Evolution mode.");
      return evolution(args);
    }

    if (arg.equals("create-evolution")) {
      log.info("Creating evolution directory.");
      return EvolutionDirectoryCreator.createEvolutionDirectory(args);
    }

    if (arg.equals("create-evolution-gui")) {
      log.info("Creating evolution directory (GUI).");
      return CreateEvolutionGUI.createEvolutionDirectory(args);
    }

    log.error("Arguments didn't match anything we know how to parse.");
    return null;
  }

  private static EnvironmentController evolution(String[] args) {
    if (args.length < 2) {
      log.error("expected second parameter to be the file pointing to evolution.properties");
      return null;
    }

    Path evolutionProperties = Paths.get(args[1]).toAbsolutePath();
    Path evolutionDirectory = evolutionProperties.getParent();
    Path evolutionFile = evolutionDirectory.resolve(EvolutionController.POPULATIONS_FILE_NAME);
    Collection<Genepool> seedPopulation = Collections.emptyList();

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
}
