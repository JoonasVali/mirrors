package ee.joonasvali.mirrors.command;

import ee.joonasvali.mirrors.EnvironmentController;
import ee.joonasvali.mirrors.EvolutionController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class EvolutionDirectoryCreator {
  private final static Logger log = LoggerFactory.getLogger(EvolutionDirectoryCreator.class);
  private static final String EVOLVE_FILE_NAME = "evolve.bat";
  private static final String PLAY_SAMPLE_FILE_NAME = "run-fittest.bat";
  public static final String EVOLUTION_WIN_PROPERTIES = "evolution.win.properties";

  public static EnvironmentController createEvolutionDirectory(String[] args) {
    if (args.length < 2) {
      log.error("expected parameter to be the file pointing to target evolution directory");
      System.exit(-1);
    }

    String evolutionDirectoryPath = args[1];
    Path evolutionDirectory = Paths.get(evolutionDirectoryPath);
    createEvolutionDirectory(evolutionDirectory);

    return null;
  }

  public static void createEvolutionDirectory(Path evolutionDirectory) {
    createAndValidateEvolutionDirectory(evolutionDirectory);

    Path mirrorsHome = getMirrorsHome();
    createEvolutionRunner(evolutionDirectory, mirrorsHome);
    createSamplePlayer(evolutionDirectory, mirrorsHome);
    createEvolutionProperties(evolutionDirectory, mirrorsHome);
  }

  private static void createAndValidateEvolutionDirectory(Path evolutionDirectory) {
    if (!Files.exists(evolutionDirectory)) {
      // If no directory is present then create it.
      try {
        Files.createDirectories(evolutionDirectory);
      } catch (IOException e) {
        log.error("Unable to create evolution directory " + evolutionDirectory, e);
        throw new RuntimeException(e);
      }
    } else {
      // If existing empty directory is present then accept it as evolution directory.
      try {
        if (Files.list(evolutionDirectory).count() != 0) {
          throw new IllegalArgumentException("Directory already exists and is not empty.");
        }
      } catch (IOException e) {
        log.error("Unable to read contents of evolution directory " + evolutionDirectory, e);
      }
    }
  }

  public static Path getMirrorsHome() {
    try {
      // This is an assumption that the script is launched from HOME/bin dir.
      // TODO read from environment variable if present
      return Paths.get("..").toRealPath();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void createEvolutionProperties(Path evolutionDirectory, Path mirrorsHome) {
    try {
      Path sourcePropertyFile = mirrorsHome.resolve("config").resolve(EVOLUTION_WIN_PROPERTIES);
      Path evolutionPropertyFile = evolutionDirectory.resolve(EvolutionController.EVOLUTION_PROPERTIES_FILE_NAME);
      Files.copy(sourcePropertyFile, evolutionPropertyFile);
    } catch (IOException e) {
      log.error("Unable to create evolution directory.", e);
      throw new RuntimeException(e);
    }
  }

  private static void createEvolutionRunner(Path evolutionDirectory, Path mirrorsHome) {
    Path runEvolutionFile = evolutionDirectory.resolve(EVOLVE_FILE_NAME);

    String content = "" +
        "@echo off\n" +
        "SET MIRRORS_HOME=" + mirrorsHome.toString() + "\n" +
        "java -jar -Xmx2048M %MIRRORS_HOME%/lib/mirrors.jar evolution ./evolution.properties";

    createFileOrFail(runEvolutionFile, content);
  }

  private static void createSamplePlayer(Path evolutionDirectory, Path mirrorsHome) {
    Path playSampleFile = evolutionDirectory.resolve(PLAY_SAMPLE_FILE_NAME);

    String content = "" +
        "@echo off\n" +
        "SET MIRRORS_HOME=" + mirrorsHome.toString() + "\n" +
        "java -jar -Xmx2048M %MIRRORS_HOME%/lib/mirrors.jar load " +
        evolutionDirectory.resolve(EvolutionController.SAMPLE_DIRECTORY_NAME).toAbsolutePath() + "\n";

    createFileOrFail(playSampleFile, content);
  }

  private static void createFileOrFail(Path file, String content) {
    try {
      Files.write(file, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
