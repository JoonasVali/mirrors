package ee.joonasvali.mirrors.command;

import ee.joonasvali.mirrors.DemoEnvironmentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Path;

public class CreateEvolutionGUI {
  private static final Logger log = LoggerFactory.getLogger(CreateEvolutionGUI.class);
  private static final String FRAME_TEXT = "Select home folder for new evolution";
  private static final String APPROVE_BUTTON_TEXT = "Select folder";

  public static DemoEnvironmentController createEvolutionDirectory(String[] args) {
    if (GraphicsEnvironment.isHeadless()) {
      log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
      System.exit(-1);
    }

    Path mirrorsHome = EvolutionDirectoryCreator.getMirrorsHome();
    JFileChooser fileChooser = new JFileChooser(mirrorsHome.toFile());
    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    fileChooser.showDialog(new JFrame(FRAME_TEXT), APPROVE_BUTTON_TEXT);
    File file = fileChooser.getSelectedFile();

    if (file == null) {
      return null;
    }

    if (!file.exists()) {
      file.mkdir();
    }

    if (!file.isDirectory()) {
      throw new RuntimeException("Selected file wasn't directory: " + file);
    }

    EvolutionDirectoryCreator.createEvolutionDirectory(file.toPath());

    return null;
  }

}
