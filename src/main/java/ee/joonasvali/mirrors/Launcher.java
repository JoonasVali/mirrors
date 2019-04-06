package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.command.CreateEvolutionGUI;
import ee.joonasvali.mirrors.command.EvolutionDirectoryCreator;
import ee.joonasvali.mirrors.command.GUIFileChooser;
import ee.joonasvali.mirrors.command.RunEvolution;
import ee.joonasvali.mirrors.command.RunRandomScene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Joonas Vali April 2016
 */
public class Launcher {
  private static final Logger log = LoggerFactory.getLogger(Launcher.class);

  public static void main(String[] args) throws InvocationTargetException, InterruptedException {
    ModelController env = getEnvironment(args);

    if (env == null) {
      log.debug("ModelController not present. Exiting.");
      System.exit(0);
    }

    WindowController controller = new WindowController(env);
    controller.launch();
  }

  private static ModelController getEnvironment(String[] args) {
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
      return RunEvolution.evolution(args);
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
}
