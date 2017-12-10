package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.scene.Constants;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.GenepoolProvider;
import ee.joonasvali.mirrors.scene.genetic.GeneticEnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.impl.GeneratorGenepoolProvider;
import ee.joonasvali.mirrors.scene.genetic.impl.LoaderGenepoolProvider;
import ee.joonasvali.mirrors.scene.genetic.impl.SerializationUtil;
import ee.joonasvali.mirrors.util.KeepAliveUtil;
import ee.joonasvali.mirrors.util.PolFileChooser;
import ee.joonasvali.mirrors.watchmaker.GenepoolCanditateFactory;
import ee.joonasvali.mirrors.watchmaker.MutationOperator;
import ee.joonasvali.mirrors.watchmaker.SystemEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.*;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.TruncationSelection;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.util.Random;

/**
 * @author Joonas Vali April 2016
 */
public class Launcher {
  private static final Logger log = LoggerFactory.getLogger(Launcher.class);
  private static String nl = System.lineSeparator();

  public static void main(String[] args) throws InvocationTargetException, InterruptedException {
    if (GraphicsEnvironment.isHeadless()) {
      log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
      System.exit(-1);
    }

    EnvironmentController env = getEnvironment(args);
    WindowController controller = new WindowController(env);
    controller.launch();
  }

  private static EnvironmentController getEnvironment(String[] args) {
    if (args.length == 0) {
      log.info("No program arguments, launching random demo scene.");
      Random random = new MersenneTwisterRNG();
      GeneFactory geneFactory = new GeneFactory(createDefaultEvolutionProperties(), random);
      EnvironmentBuilder builder = new GeneticEnvironmentBuilder(new GeneratorGenepoolProvider(geneFactory, 750, 550));
      return new DemoEnvironmentController(builder);
    }

    String arg = args[0];
    if (arg.equals("load")) {
      log.info("Launching GUI file loader.");
      return chooseFileWithGUI();
    }

    if (arg.equals("evolution")) {
      log.info("Launching Evolution mode.");
      return evolution(args);
    }

    log.error("Arguments didn't match anything we know how to parse.");
    return null; // satisfy compiler
  }

  private static EvolutionProperties createDefaultEvolutionProperties() {
    return new EvolutionProperties(
        DefaultEvolutionPropertyValues.ELITES,
        DefaultEvolutionPropertyValues.CONCURRENT,
        DefaultEvolutionPropertyValues.TARGET_FITNESS,
        DefaultEvolutionPropertyValues.KEEP_MACHINE_ALIVE,
        new File("../../saved"),
        DefaultEvolutionPropertyValues.GENE_MUTATION_RATE,
        DefaultEvolutionPropertyValues.GENE_ADDITION_RATE,
        DefaultEvolutionPropertyValues.GENE_DELETION_RATE,
        DefaultEvolutionPropertyValues.REFLECTORS_ENABLED,
        DefaultEvolutionPropertyValues.BENDERS_ENABLED,
        DefaultEvolutionPropertyValues.ACCELERATORS_ENABLED,
        DefaultEvolutionPropertyValues.REPELLENTS_ENABLED
    );
  }

  private static EnvironmentController evolution(String[] args) {
    if (args.length < 2) {
      log.error("expected second argument to be the file pointing to evolution.properties");
      return null;
    }
    EvolutionProperties properties = loadProperties(args[1]);
    if (properties == null) {
      log.error("properties == null");
      return null;
    }

    File file = properties.getSavingDir();
    log.info("Files saved to: " + file);

    Random random = new MersenneTwisterRNG();
    final SerializationUtil saver = new SerializationUtil(file);
    GeneFactory geneFactory = new GeneFactory(properties, random);
    GenepoolProvider randomProvider = getProvider(geneFactory);
    GenepoolCanditateFactory candidateFactory = new GenepoolCanditateFactory(randomProvider);

    java.util.List<EvolutionaryOperator<Genepool>> operators = getEvolutionaryOperators(geneFactory, properties);
    EvolutionPipeline<Genepool> pipeline = new EvolutionPipeline<>(operators);

    EvolutionEngine<Genepool> engine
        = new GenerationalEvolutionEngine<>(
        candidateFactory,
        pipeline,
        new SystemEvaluator(),
        new TruncationSelection(0.5),
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

      saver.store(winner, "winner");
      log.info("Evolution completed.");
    } catch (Exception ex) {
      log.error("Fatal error during evolution", ex);
      return null;
    }

    EnvironmentBuilder builder = new GeneticEnvironmentBuilder(new LoaderGenepoolProvider(winner));
    return new DemoEnvironmentController(builder);
  }

  private static EvolutionProperties loadProperties(String name) {
    EvolutionProperties properties = null;
    try {
      File userDir = new File(System.getProperty("user.dir"));
      File propertiesFile = new File(userDir, name).getCanonicalFile();
      log.info("Properties read from: " + propertiesFile);
      Properties props = new Properties();
      try (InputStream stream = new FileInputStream(propertiesFile)) {
        props.load(stream);
      }

      File savedDir = new File(userDir, props.getProperty("output.dir", "../../saved")).getCanonicalFile();

      properties = new EvolutionProperties(
          Integer.parseInt(props.getProperty("elites", String.valueOf(DefaultEvolutionPropertyValues.ELITES))),
          Integer.parseInt(props.getProperty("concurrent", String.valueOf(DefaultEvolutionPropertyValues.CONCURRENT))),
          Integer.parseInt(props.getProperty("target.fitness", String.valueOf(DefaultEvolutionPropertyValues.TARGET_FITNESS))),
          Boolean.parseBoolean(props.getProperty("keep.machine.alive", String.valueOf(DefaultEvolutionPropertyValues.KEEP_MACHINE_ALIVE))),
          savedDir,
          Double.parseDouble(props.getProperty("gene.mutation.rate", String.valueOf(DefaultEvolutionPropertyValues.GENE_MUTATION_RATE))),
          Double.parseDouble(props.getProperty("gene.deletion.rate", String.valueOf(DefaultEvolutionPropertyValues.GENE_DELETION_RATE))),
          Double.parseDouble(props.getProperty("gene.addition.rate", String.valueOf(DefaultEvolutionPropertyValues.GENE_ADDITION_RATE))),
          Boolean.parseBoolean(props.getProperty("reflectors.enabled", String.valueOf(DefaultEvolutionPropertyValues.REFLECTORS_ENABLED))),
          Boolean.parseBoolean(props.getProperty("benders.enabled", String.valueOf(DefaultEvolutionPropertyValues.BENDERS_ENABLED))),
          Boolean.parseBoolean(props.getProperty("accelerators.enabled", String.valueOf(DefaultEvolutionPropertyValues.ACCELERATORS_ENABLED))),
          Boolean.parseBoolean(props.getProperty("repellents.enabled", String.valueOf(DefaultEvolutionPropertyValues.REPELLENTS_ENABLED)))
      );
    } catch (IOException e) {
      log.error("Can't read properties " + name, e);
      System.exit(-1);
    }

    return properties;
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
          saver.store(data.getBestCandidate(), data.getGenerationNumber() + "-" + (int) (data.getBestCandidateFitness()));
          last = data.getBestCandidateFitness();
        }
      }
    };
  }

  /**
   * Possible to add more operators here, read about them in Watchmaker docs.
   * http://watchmaker.uncommons.org/api/org/uncommons/watchmaker/framework/operators/package-summary.html
   */
  private static ArrayList<EvolutionaryOperator<Genepool>> getEvolutionaryOperators(GeneFactory geneFactory, EvolutionProperties properties) {
    ArrayList<EvolutionaryOperator<Genepool>> operators = new ArrayList<>();
//    ListCrossover e = new ListCrossover(3, new Probability(0.1));
    operators.add(new MutationOperator(geneFactory, properties.getGeneAdditionRate(), properties.getGeneDeletionRate()));
    //operators.add(e);
    return operators;
  }

  private static DemoEnvironmentController chooseFileWithGUI() {
    PolFileChooser fileChooser = new PolFileChooser();
    File file = fileChooser.selectFile(System.getProperty("user.dir"));
    if (file == null) {
      log.error("No file selected");
      System.exit(-1);
    } else {
      EnvironmentBuilder builder = new GeneticEnvironmentBuilder(new LoaderGenepoolProvider(file));
      return new DemoEnvironmentController(builder);
    }
    return null;
  }

}
