package ee.joonasvali.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * @author Joonas Vali April 2016
 */
public class PolFileChooser extends JFileChooser {
  private static final Logger log = LoggerFactory.getLogger(PolFileChooser.class);
  public PolFileChooser() {
    setDialogTitle("Select *.pol file");
    setFileFilter(new FileFilter() {
      @Override
      public boolean accept(File f) {
        if (f.isDirectory()) {
          return true;
        }

        return f.isFile() && f.getName().endsWith(".pol");
      }

      @Override
      public String getDescription() {
        return ".pol scene files";
      }
    });
    setMultiSelectionEnabled(false);
  }

  public File selectFile(String selectedDir) {
    File currentDir = new File(selectedDir);
    if (!currentDir.isDirectory()) {
      log.warn("selected dir is not a dir: " + currentDir);
    } else {
      setCurrentDirectory(currentDir);
    }
    showDialog(new JFrame(), "Select");
    return getSelectedFile();
  }
}
