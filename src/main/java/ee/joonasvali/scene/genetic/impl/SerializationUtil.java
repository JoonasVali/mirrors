package ee.joonasvali.scene.genetic.impl;

import ee.joonasvali.scene.genetic.Genepool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


public class SerializationUtil {
  private static Logger log = LoggerFactory.getLogger(SerializationUtil.class);
  private final File dir;

  public SerializationUtil(File dir) {
    this.dir = dir;
  }

  public String initTime = String.valueOf(System.currentTimeMillis());
  public void store(Genepool pool, String name) {
    File folder = new File(dir, initTime);
    folder.mkdirs();
    File file = new File(folder, name + ".pol");
    try (
        FileOutputStream fileOut = new FileOutputStream(file);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
    ) {
      out.writeObject(pool);
      log.info("saved pool to " + file.getAbsolutePath());
    } catch (IOException e) {
      log.error("Fatal error: ", e);
    }
  }

  public static Genepool get(File file) {
    try (
        FileInputStream fileIn = new FileInputStream(file);
        ObjectInputStream in = new ObjectInputStream(fileIn);
    ) {
      Genepool pool = (Genepool) in.readObject();
      return pool;
    } catch (IOException | ClassNotFoundException e) {
      log.error("Fatal error: ", e);
    }
    return null;
  }

}
