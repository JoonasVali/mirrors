package ee.joonasvali.mirrors.setup;

import ee.joonasvali.mirrors.ModelController;
import ee.joonasvali.mirrors.WindowController;
import ee.joonasvali.mirrors.setup.command.CreateEvolutionGUI;
import ee.joonasvali.mirrors.setup.command.EvolutionDirectoryCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Joonas Vali April 2016
 */
public class Launcher {
  private static final Logger log = LoggerFactory.getLogger(Launcher.class);

  public static void main(String[] args) throws InvocationTargetException, InterruptedException {
    ModelController modelController = getModelController(args);

    if (modelController == null) {
      log.debug("ModelController not present. Exiting.");
      System.exit(0);
    }

    WindowController controller = new WindowController(modelController);
    controller.launch();
  }

  private static ModelController getModelController(String[] args) {
    if (args.length == 0) {
      throw new IllegalArgumentException("Must provide arguments.");
    }

    String arg = args[0];

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
