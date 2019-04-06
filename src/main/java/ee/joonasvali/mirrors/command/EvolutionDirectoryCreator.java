package ee.joonasvali.mirrors.command;

import ee.joonasvali.mirrors.ModelController;
import ee.joonasvali.mirrors.EvolutionController;
import org.apache.commons.lang3.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

public class EvolutionDirectoryCreator {
  public static final String MIRRORS_HOME_ENV_VARIABLE = "MIRRORS_HOME";

  private static final Logger log = LoggerFactory.getLogger(EvolutionDirectoryCreator.class);
  private static final boolean isWin = SystemUtils.IS_OS_WINDOWS;
  private static final String EVOLVE_FILE_NAME = "evolve" + (isWin ? ".bat" : ".sh");
  private static final String PLAY_SAMPLE_FILE_NAME = "play" + (isWin ? ".bat" : ".sh");

  private static final String EVOLUTION_WIN_PROPERTIES = "evolution.win.properties";
  private static final String EVOLUTION_UNIX_PROPERTIES = "evolution.unix.properties";
  private static final String PROPERTIES_FILE_NAME = isWin ? EVOLUTION_WIN_PROPERTIES : EVOLUTION_UNIX_PROPERTIES;
  public static final String PLAY_DEMO_NAME = "demo" + (isWin ? ".bat" : ".sh");

  public static ModelController createEvolutionDirectory(String[] args) {
    if (args.length < 2) {
      log.error("expected parameter to be the file pointing to target evolution directory");
      System.exit(-1);
    }

    String evolutionDirectoryPath = args[1];

    try {
      Path evolutionDirectory = Paths.get(evolutionDirectoryPath).toRealPath();
      createEvolutionDirectory(evolutionDirectory);
      return null;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public static void createEvolutionDirectory(Path evolutionDirectory) {
    createAndValidateEvolutionDirectory(evolutionDirectory);

    Path mirrorsHome = getMirrorsHome();
    createEvolutionRunner(evolutionDirectory, mirrorsHome);
    createSamplePlayer(evolutionDirectory, mirrorsHome);
    createEvolutionProperties(evolutionDirectory, mirrorsHome);
    createRandomDemoPlayer(evolutionDirectory, mirrorsHome);
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
      String mirrorsHome = System.getenv(MIRRORS_HOME_ENV_VARIABLE);
      if (mirrorsHome != null) {
        Path result = Paths.get(mirrorsHome);
        if (Files.exists(result)) {
          return result;
        } else {
          log.error("Invalid MIRRORS_HOME defined: " + mirrorsHome);
        }
      }
      // This is an assumption that the script is launched from HOME/bin dir.
      return Paths.get("..").toRealPath();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private static void createEvolutionProperties(Path evolutionDirectory, Path mirrorsHome) {
    try {
      Path sourcePropertyFile = mirrorsHome.resolve("config").resolve(PROPERTIES_FILE_NAME);
      Path evolutionPropertyFile = evolutionDirectory.resolve(EvolutionController.EVOLUTION_PROPERTIES_FILE_NAME);
      Files.copy(sourcePropertyFile, evolutionPropertyFile);
    } catch (IOException e) {
      log.error("Unable to create evolution directory.", e);
      throw new RuntimeException(e);
    }
  }

  private static void createEvolutionRunner(Path evolutionDirectory, Path mirrorsHome) {
    Path runEvolutionFile = evolutionDirectory.resolve(EVOLVE_FILE_NAME);

    String content;

    if (isWin) {
      content = "" +
          "@echo off\n" +
          "SET MIRRORS_HOME=" + mirrorsHome.toString() + "\n" +
          "java -jar -Xmx2048M %MIRRORS_HOME%\\lib\\mirrors.jar evolution ./evolution.properties";
    } else {
      content = "" +
          "#!/bin/bash\n" +
          "export MIRRORS_HOME=" + mirrorsHome.toString() + "\n" +
          "java -jar -Xmx2048M $MIRRORS_HOME/lib/mirrors.jar evolution ./evolution.properties";
    }

    createFileOrFail(runEvolutionFile, content);
  }

  private static void createSamplePlayer(Path evolutionDirectory, Path mirrorsHome) {
    Path playSampleFile = evolutionDirectory.resolve(PLAY_SAMPLE_FILE_NAME);

    String content;
    if (isWin) {
      content = "" +
          "@echo off\n" +
          "SET MIRRORS_HOME=" + mirrorsHome.toString() + "\n" +
          "java -jar -Xmx2048M %MIRRORS_HOME%/lib/mirrors.jar load " +
          evolutionDirectory.resolve(EvolutionController.SAMPLE_DIRECTORY_NAME).toAbsolutePath() + "\n";
    } else {
      content = "" +
          "#!/bin/bash\n" +
          "export MIRRORS_HOME=" + mirrorsHome.toString() + "\n" +
          "java -jar -Xmx2048M $MIRRORS_HOME/lib/mirrors.jar load" +
          evolutionDirectory.resolve(EvolutionController.SAMPLE_DIRECTORY_NAME).toAbsolutePath() + "\n";
    }
    createFileOrFail(playSampleFile, content);
  }

  private static void createRandomDemoPlayer(Path evolutionDirectory, Path mirrorsHome) {
    Path playDemoFile = evolutionDirectory.resolve(PLAY_DEMO_NAME);

    String content;
    if (isWin) {
      content = "" +
          "@echo off\n" +
          "SET MIRRORS_HOME=" + mirrorsHome.toString() + "\n" +
          "java -jar -Xmx2048M %MIRRORS_HOME%/lib/mirrors.jar";
    } else {
      content = "" +
          "#!/bin/bash\n" +
          "export MIRRORS_HOME=" + mirrorsHome.toString() + "\n" +
          "java -jar -Xmx2048M $MIRRORS_HOME/lib/mirrors.jar";
    }
    createFileOrFail(playDemoFile, content);
  }

  private static void createFileOrFail(Path file, String content) {
    try {
      Files.write(file, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }
}
