package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.scene.Constants;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import ee.joonasvali.mirrors.scene.genetic.GenomeProvider;
import ee.joonasvali.mirrors.scene.genetic.impl.GeneratorGenomeProvider;
import ee.joonasvali.mirrors.scene.genetic.util.SerializationUtil;
import ee.joonasvali.mirrors.util.KeepAliveUtil;
import ee.joonasvali.mirrors.watchmaker.GenomeCanditateFactory;
import ee.joonasvali.mirrors.watchmaker.MutationOperator;
import ee.joonasvali.mirrors.watchmaker.SystemEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.EvaluatedCandidate;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.TerminationCondition;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.TargetFitness;
import org.uncommons.watchmaker.framework.termination.UserAbort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class EvolutionController {
  public static final String EVOLUTION_PROPERTIES_FILE_NAME = "evolution.properties";
  public static final String POPULATIONS_FILE_NAME = "populations.json";
  public static final String SAMPLE_DIRECTORY_NAME = "samples";
  private final static String nl = System.lineSeparator();
  private final static Logger log = LoggerFactory.getLogger(EvolutionController.class);
  private final Path evolutionDirectory;
  private final Path sampleDirectory;
  private final EvolutionProperties properties;
  private final int generationOffset;

  public EvolutionController(Path evolutionDirectory) {
    Path evolutionPropertyFile = evolutionDirectory.resolve(EVOLUTION_PROPERTIES_FILE_NAME);
    EvolutionPropertyLoader loader = new EvolutionPropertyLoader();
    this.properties = loader.loadProperties(evolutionPropertyFile);
    this.evolutionDirectory = evolutionDirectory;
    this.sampleDirectory = evolutionDirectory.resolve(SAMPLE_DIRECTORY_NAME);

    if (!Files.exists(sampleDirectory)) {
      try {
        Files.createDirectory(sampleDirectory);
      } catch (IOException e) {
        throw new RuntimeException("Unable to create sample directory.", e);
      }
    }

    this.generationOffset = lookupLargestGeneration(sampleDirectory) + 1;
    if (this.generationOffset != 0) {
      log.info("Continuing evolution from generation " + (generationOffset));
    }
  }

  public Optional<Genome> runEvolution(Collection<Genome> seedPopulation) {
    log.info("Best improvement of every generation saved to: " + sampleDirectory);
    final SerializationUtil serializer = new SerializationUtil(sampleDirectory);

    if (properties.isKeepAlive()) {
      log.info("KeepAliveUtil activated");
      KeepAliveUtil.keepAlive();
    }

    UserAbort abortCondition = new UserAbort();

    Thread shutdownHook = new AbortOnShutdown(() -> {
      abortCondition.abort();
      // Wait until main thread successfully aborts and saves the state, by holding their shared lock.
      synchronized (abortCondition) {
        log.info("Aborting completed, continuing with shutdown.");
      }
    });

    Runtime.getRuntime().addShutdownHook(shutdownHook);

    Random random = new MersenneTwisterRNG();
    GeneFactory geneFactory = new GeneFactory(properties, random);
    GenomeProvider randomProvider = getProvider(geneFactory, properties);
    GenomeCanditateFactory candidateFactory = new GenomeCanditateFactory(randomProvider);

    List<EvolutionaryOperator<Genome>> operators = getEvolutionaryOperators(geneFactory, properties);
    EvolutionPipeline<Genome> pipeline = new EvolutionPipeline<>(operators);

    EvolutionEngine<Genome> engine
        = new GenerationalEvolutionEngine<>(
        candidateFactory,
        pipeline,
        new SystemEvaluator(),
        new RouletteWheelSelection(),
        random);

    engine.addEvolutionObserver(getEvolutionObserver(serializer, generationOffset));
    int targetFitness = properties.getTargetFitness();
    int concurrent = properties.getConcurrent();
    int elite = properties.getElites();

    log.info("Starting evolution process with target fitness " + nl + targetFitness + "." + nl +
        "Concurrent organisms: " + concurrent + nl + "Elite population: " + elite + nl);

    TerminationCondition targetFitnessCondition = new TargetFitness(targetFitness, true);

    Genome winner;
    try {
      synchronized (abortCondition) {
        // This is a blocking call, evolution happens here.
        List<EvaluatedCandidate<Genome>> population = engine.evolvePopulation(
            concurrent, elite, seedPopulation, targetFitnessCondition, abortCondition
        );

        List<TerminationCondition> conditions = engine.getSatisfiedTerminationConditions();
        if (conditions.contains(abortCondition) && !conditions.contains(targetFitnessCondition)) {
          SerializationUtil populationSerializer = new SerializationUtil(evolutionDirectory);
          populationSerializer.serializePopulation(
              population.stream()
                  .map(EvaluatedCandidate::getCandidate)
                  .collect(Collectors.toList()),
              evolutionDirectory.resolve(POPULATIONS_FILE_NAME)
          );
          return Optional.empty();
        }

        Optional<EvaluatedCandidate<Genome>> winnerCandidate = population.stream()
            .filter(genome -> genome.getFitness() >= targetFitness)
            .max(Comparator.comparingDouble(EvaluatedCandidate::getFitness));

        if (!winnerCandidate.isPresent()) {
          log.error("Something went wrong. Couldn't find winner that satisfies targetFitness. Returning best from population.");
          winnerCandidate = population.stream()
              .max(Comparator.comparingDouble(EvaluatedCandidate::getFitness));
        }

        if (winnerCandidate.isPresent()) {
          winner = winnerCandidate.get().getCandidate();
        } else {
          Runtime.getRuntime().removeShutdownHook(shutdownHook);
          throw new IllegalStateException("No population. Everyone's dead.");
        }

        serializer.serialize(winner, "winner");
        log.info("Evolution completed.");
      }
    } catch (Exception ex) {
      log.error("Fatal error during evolution", ex);
      Runtime.getRuntime().removeShutdownHook(shutdownHook);
      return Optional.empty();
    }

    Runtime.getRuntime().removeShutdownHook(shutdownHook);
    return Optional.of(winner);
  }

  private static GenomeProvider getProvider(GeneFactory geneFactory, EvolutionProperties properties) {
    return new GeneratorGenomeProvider(
        geneFactory,
        Constants.DIMENSION_X,
        Constants.DIMENSION_Y,
        properties.isTopProducerEnabled(),
        properties.isMiddleProducerEnabled(),
        properties.isBottomProducerEnabled()
    );
  }

  private static EvolutionObserver<? super Genome> getEvolutionObserver(SerializationUtil saver, int generationOffset) {
    return new EvolutionObserver<Genome>() {
      private volatile double last = 0;

      @Override
      public void populationUpdate(PopulationData<? extends Genome> data) {
        int generation = data.getGenerationNumber() + generationOffset;
        log.info("Time: " + new Date());
        log.info("best of generation (" + generation + "): " + data.getBestCandidateFitness());
        if (data.getBestCandidateFitness() > last) {
          try {
            saver.serialize(data.getBestCandidate(), generation + "-" + (int) (data.getBestCandidateFitness()));
          } catch (IOException e) {
            log.error("Unable to save best candidate.", e);
          }
          last = data.getBestCandidateFitness();
        }
      }
    };
  }

  private static ArrayList<EvolutionaryOperator<Genome>> getEvolutionaryOperators(GeneFactory geneFactory, EvolutionProperties properties) {
    ArrayList<EvolutionaryOperator<Genome>> operators = new ArrayList<>();
    operators.add(new MutationOperator(geneFactory, properties.getGeneAdditionRate(), properties.getGeneDeletionRate()));
    return operators;
  }

  private static int lookupLargestGeneration(Path sampleDirectory) {
    try {
      return Files.list(sampleDirectory)
          .map(path -> path.getFileName().toString())
          .filter(name -> name.matches("\\d+-\\d+\\.json"))
          .map(name -> name.split("-")[0])
          .map((Integer::parseInt))
          .max(Integer::compareTo)
          .orElse(0);
    } catch (IOException e) {
      log.error("Unable to look for largest generation, starting from 0.", e);
      return 0;
    }
  }

  private static class AbortOnShutdown extends Thread {
    private static final Logger log = LoggerFactory.getLogger(AbortOnShutdown.class);
    private final Runnable abort;

    public AbortOnShutdown(Runnable abort) {
      this.abort = abort;
    }

    @Override
    public void run() {
      log.debug("Abort shutdown hook initiated!");
      abort.run();
      log.debug("Abort shutdown hook completed!");
    }
  }
}
