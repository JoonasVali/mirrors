package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.scene.Constants;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.GenepoolProvider;
import ee.joonasvali.mirrors.scene.genetic.impl.GeneratorGenepoolProvider;
import ee.joonasvali.mirrors.scene.genetic.util.SerializationUtil;
import ee.joonasvali.mirrors.util.KeepAliveUtil;
import ee.joonasvali.mirrors.watchmaker.GenepoolCanditateFactory;
import ee.joonasvali.mirrors.watchmaker.MutationOperator;
import ee.joonasvali.mirrors.watchmaker.SystemEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.RouletteWheelSelection;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class EvolutionController {
  private final static String nl = System.lineSeparator();
  private final static Logger log = LoggerFactory.getLogger(EvolutionController.class);
  private final EvolutionProperties properties;

  public EvolutionController(EvolutionProperties properties) {
    this.properties = properties;
  }

  public Genepool runEvolution() {
    File file = properties.getSavingDir();
    log.info("Files saved to: " + file);

    Random random = new MersenneTwisterRNG();
    final SerializationUtil saver = new SerializationUtil(file.toPath(), String.valueOf(System.currentTimeMillis()));
    GeneFactory geneFactory = new GeneFactory(properties, random);
    GenepoolProvider randomProvider = getProvider(geneFactory);
    GenepoolCanditateFactory candidateFactory = new GenepoolCanditateFactory(randomProvider);

    List<EvolutionaryOperator<Genepool>> operators = getEvolutionaryOperators(geneFactory, properties);
    EvolutionPipeline<Genepool> pipeline = new EvolutionPipeline<>(operators);

    EvolutionEngine<Genepool> engine
        = new GenerationalEvolutionEngine<>(
        candidateFactory,
        pipeline,
        new SystemEvaluator(),
        new RouletteWheelSelection(),
        random);
    engine.addEvolutionObserver(getEvolutionObserver(saver));
    int targetFitness = properties.getTargetFitness();
    int concurrent = properties.getConcurrent();
    int elite = properties.getElites();

    if (properties.isKeepAlive()) {
      log.info("KeepAliveUtil activated");
      KeepAliveUtil.keepAlive();
    }

    log.info("Starting evolution process with target fitness " + nl + targetFitness + "." + nl +
        "Concurrent organisms: " + concurrent + nl + "Elite population: " + elite + nl);

    Genepool winner;
    try {
      // This is a blocking call, evolution happens here.
      winner = engine.evolve(concurrent, elite, new TargetFitness(targetFitness, true));

      saver.serialize(winner, "winner");
      log.info("Evolution completed.");
    } catch (Exception ex) {
      log.error("Fatal error during evolution", ex);
      return null;
    }

    return winner;
  }

  private static GenepoolProvider getProvider(GeneFactory geneFactory) {
    return new GeneratorGenepoolProvider(geneFactory, Constants.DIMENSION_X, Constants.DIMENSION_Y);
  }

  private static EvolutionObserver<? super Genepool> getEvolutionObserver(SerializationUtil saver) {
    return new EvolutionObserver<Genepool>() {
      double last = 0;

      @Override
      public void populationUpdate(PopulationData<? extends Genepool> data) {
        log.info("Time: " + SimpleDateFormat.getDateTimeInstance().format(new Date(System.currentTimeMillis())));
        log.info("best of generation (" + data.getGenerationNumber() + "): " + data.getBestCandidateFitness());
        if (data.getBestCandidateFitness() > last) {
          try {
            saver.serialize(data.getBestCandidate(), data.getGenerationNumber() + "-" + (int) (data.getBestCandidateFitness()));
          } catch (IOException e) {
            log.error("Unable to save best candidate.", e);
          }
          last = data.getBestCandidateFitness();
        }
      }
    };
  }

  private static ArrayList<EvolutionaryOperator<Genepool>> getEvolutionaryOperators(GeneFactory geneFactory, EvolutionProperties properties) {
    ArrayList<EvolutionaryOperator<Genepool>> operators = new ArrayList<>();
    operators.add(new MutationOperator(geneFactory, properties.getGeneAdditionRate(), properties.getGeneDeletionRate()));
    return operators;
  }

}
