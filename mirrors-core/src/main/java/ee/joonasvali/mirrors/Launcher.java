package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.command.GUIFileChooser;
import ee.joonasvali.mirrors.command.RunEvolution;
import ee.joonasvali.mirrors.command.RunMutationDemo;
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

    if (arg.equals("mutation")) {
      log.info("Launching mutation demo.");
      return RunMutationDemo.run(args);
    }

    log.error("Arguments didn't match anything we know how to parse.");
    return null;
  }
}
