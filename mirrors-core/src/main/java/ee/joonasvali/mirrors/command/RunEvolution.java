package ee.joonasvali.mirrors.command;

import ee.joonasvali.mirrors.DemoModelController;
import ee.joonasvali.mirrors.ModelController;
import ee.joonasvali.mirrors.EvolutionController;
import ee.joonasvali.mirrors.scene.ModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import ee.joonasvali.mirrors.scene.genetic.GeneticModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.impl.LoaderGenomeProvider;
import ee.joonasvali.mirrors.scene.genetic.util.SerializationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public class RunEvolution {
  private static final Logger log = LoggerFactory.getLogger(RunEvolution.class);

  public static ModelController evolution(String[] args) {
    if (args.length < 2) {
      log.error("expected second parameter to be the file pointing to evolution.properties");
      return null;
    }

    Path evolutionProperties = Paths.get(args[1]).toAbsolutePath();
    Path evolutionDirectory = evolutionProperties.getParent();
    Path evolutionFile = evolutionDirectory.resolve(EvolutionController.POPULATIONS_FILE_NAME);
    Collection<Genome> seedPopulation = Collections.emptyList();

    if (Files.exists(evolutionFile)) {
      log.info("Loaded existing population from evolution file: " + evolutionFile);
      try {
        seedPopulation = SerializationUtil.deserializePopulation(evolutionFile);
      } catch (IOException e) {
        log.error("Unable to read evolution file.");
        throw new RuntimeException(e);
      }
    }

    EvolutionController controller = new EvolutionController(evolutionDirectory);
    Optional<Genome> winner = controller.runEvolution(seedPopulation);
    if (winner.isPresent()) {
      ModelBuilder builder = new GeneticModelBuilder(new LoaderGenomeProvider(winner.get()));
      return new DemoModelController(builder);
    }
    return null;
  }
}
