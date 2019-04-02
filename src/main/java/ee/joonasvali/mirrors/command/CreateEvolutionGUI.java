package ee.joonasvali.mirrors.command;

import ee.joonasvali.mirrors.DemoEnvironmentController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class CreateEvolutionGUI {
  private static final Logger log = LoggerFactory.getLogger(CreateEvolutionGUI.class);
  private static final String FRAME_TEXT = "Select home folder for new evolution";
  private static final String APPROVE_BUTTON_TEXT = "Select folder";
  private static final String PROJECTS_FOLDER_NAME = "projects";

  public static DemoEnvironmentController createEvolutionDirectory(String[] args) {
    if (GraphicsEnvironment.isHeadless()) {
      log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
      System.exit(-1);
    }

    Path mirrorsHome = EvolutionDirectoryCreator.getMirrorsHome();
    Path projects = mirrorsHome.resolve(PROJECTS_FOLDER_NAME);

    Path file = null;
    do {
      if (file != null && file.equals(projects)) {
        JOptionPane.showMessageDialog(new JFrame(), "" +
            "The selected folder is meant to be the container folder for all evolutions. " +
            "Try creating a new folder in it or type the target folder name on the path " +
            "to be created.");
      }
      JFileChooser fileChooser = new JFileChooser(projects.toFile());
      fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
      fileChooser.showDialog(new JFrame(FRAME_TEXT), APPROVE_BUTTON_TEXT);
      file = getSelectedFile(fileChooser);
    } while (file != null && file.equals(projects));

    if (file == null) {
      return null;
    }

    if (!Files.exists(file)) {
      try {
        Files.createDirectory(file);
      } catch (IOException e) {
        throw new RuntimeException("Unable to create directory to " + file, e);
      }
    }

    if (!Files.isDirectory(file)) {
      throw new RuntimeException("Selected file wasn't directory: " + file);
    }

    EvolutionDirectoryCreator.createEvolutionDirectory(file);
    int answer = JOptionPane.showConfirmDialog(new JFrame(),
        "<html><p>Successfully created evolution directory at " + file +
            "</p><p>Would you like to open the created folder in explorer?</p></html>",
        "Evolution directory created successfully.",
        JOptionPane.YES_NO_OPTION);

    if (answer == 0) {
      try {
        Desktop.getDesktop().open(file.toFile());
      } catch (IOException e) {
        log.error("Unable to open selected directory.", e);
      }
    }

    return null;
  }

  private static Path getSelectedFile(JFileChooser chooser) {
    File result = chooser.getSelectedFile();
    if (result != null) {
      return result.toPath();
    } else {
      return null;
    }
  }
}
