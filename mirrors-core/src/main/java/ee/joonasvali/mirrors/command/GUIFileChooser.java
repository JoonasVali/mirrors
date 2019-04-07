package ee.joonasvali.mirrors.command;

import ee.joonasvali.mirrors.DemoModelController;
import ee.joonasvali.mirrors.scene.ModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.GeneticModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.impl.LoaderGenomeProvider;
import ee.joonasvali.mirrors.util.ModelFileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GUIFileChooser {
  private static final Logger log = LoggerFactory.getLogger(GUIFileChooser.class);

  public static DemoModelController chooseFileWithGUI(String[] args) {
    if (args.length < 2) {
      log.error("expected 2nd parameter to be the file pointing to sample directory");
      System.exit(-1);
    }

    if (GraphicsEnvironment.isHeadless()) {
      log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
      System.exit(-1);
    }

    Path sampleDir = Paths.get(args[1]);

    ModelFileChooser fileChooser = new ModelFileChooser();
    File file = fileChooser.selectFile(sampleDir);
    if (file == null) {
      log.error("No file selected");
      System.exit(-1);
    } else {
      ModelBuilder builder = new GeneticModelBuilder(new LoaderGenomeProvider(file));
      return new DemoModelController(builder);
    }
    return null;
  }

}
