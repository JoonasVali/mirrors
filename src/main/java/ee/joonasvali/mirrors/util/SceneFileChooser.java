package ee.joonasvali.mirrors.util;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.nio.file.Path;

/**
 * @author Joonas Vali April 2016
 */
public class SceneFileChooser extends JFileChooser {
  public SceneFileChooser() {
    setDialogTitle("Select *.json file");
    setFileFilter(new FileFilter() {
      @Override
      public boolean accept(File f) {
        if (f.isDirectory()) {
          return true;
        }

        return f.isFile() && f.getName().endsWith(".json");
      }

      @Override
      public String getDescription() {
        return ".json scene files";
      }
    });
    setMultiSelectionEnabled(false);
  }

  public File selectFile(Path sampleDir) {
    setCurrentDirectory(sampleDir.toFile());
    showDialog(new JFrame(), "Select");
    return getSelectedFile();
  }
}
