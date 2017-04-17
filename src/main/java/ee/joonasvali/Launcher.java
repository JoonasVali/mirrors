package ee.joonasvali;

import ee.joonasvali.graphics.GameContainer;
import ee.joonasvali.scene.Constants;
import ee.joonasvali.scene.EnvironmentBuilder;
import ee.joonasvali.scene.genetic.Genepool;
import ee.joonasvali.scene.genetic.GenepoolProvider;
import ee.joonasvali.scene.genetic.GeneticEnvironmentBuilder;
import ee.joonasvali.scene.genetic.impl.GeneratorGenepoolProvider;
import ee.joonasvali.scene.genetic.impl.LoaderGenepoolProvider;
import ee.joonasvali.scene.genetic.impl.SerializationUtil;
import ee.joonasvali.util.PolFileChooser;
import ee.joonasvali.watchmaker.GenepoolCanditateFactory;
import ee.joonasvali.watchmaker.Mutation;
import ee.joonasvali.watchmaker.SystemEvaluator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.uncommons.maths.random.MersenneTwisterRNG;
import org.uncommons.watchmaker.framework.EvolutionEngine;
import org.uncommons.watchmaker.framework.EvolutionObserver;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;
import org.uncommons.watchmaker.framework.GenerationalEvolutionEngine;
import org.uncommons.watchmaker.framework.PopulationData;
import org.uncommons.watchmaker.framework.operators.EvolutionPipeline;
import org.uncommons.watchmaker.framework.selection.TruncationSelection;
import org.uncommons.watchmaker.framework.termination.TargetFitness;

import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import java.awt.Dimension;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
  private static volatile boolean running = true;
  private static volatile GameContainer container;
  private static volatile Timer timer;

  public static void main(String[] args) throws InvocationTargetException, InterruptedException {
    if (GraphicsEnvironment.isHeadless()) {
      log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
      System.exit(-1);
    }


    EnvironmentController env = getEnvironment(args);

    if (env == null) {
      log.info("EnvironmentController not present. Exiting.");
      System.exit(0);
    }
    env.init();

    SwingUtilities.invokeAndWait(
        () -> {
          try {
            container = new GameContainer(env, Constants.DIMENSION_X, Constants.DIMENSION_Y);
            container.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            container.setSize(new Dimension(Constants.DIMENSION_X, Constants.DIMENSION_Y));
            container.setVisible(true);
          } catch (Exception e) {
            log.error("Unable to launch Mirrors", e);
          }
        }
    );

    // Make the graphics update.
    int delay = 50;
    ActionListener taskPerformer = evt -> container.repaint();
    timer = new Timer(delay, taskPerformer);
    timer.start();

    // Make the simulation update.
    final Object lock = env.getEnvironment().getLock();
    while (running) {
      synchronized (lock) {
        env.nextStep();
      }

      Thread.sleep(10L);
    }

  }

  private static EnvironmentController getEnvironment(String[] args) {
    if (args.length == 0) {
      log.info("No program arguments, launching random demo scene.");
      EnvironmentBuilder builder = new GeneticEnvironmentBuilder(new GeneratorGenepoolProvider(new Random(), 750, 550));
      return new DemoEnvironmentController(builder);
    }

    if (args.length > 0) {
      String arg = args[0];
      if (arg.equals("load")) {
        log.info("Launching GUI file loader.");
        return chooseFileWithGUI();
      }

      if (arg.equals("evolution")) {
        log.info("Launching Evolution mode.");
        return evolution(args);
      }
    }
    log.error("Arguments didn't match anything we know how to parse.");
    return null; // satisfy compiler
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

    final SerializationUtil saver = new SerializationUtil(file);
    GenepoolProvider randomProvider = getProvider();
    GenepoolCanditateFactory candidateFactory = new GenepoolCanditateFactory(randomProvider);

    java.util.List<EvolutionaryOperator<Genepool>> operators = getEvolutionaryOperators();
    EvolutionPipeline<Genepool> pipeline = new EvolutionPipeline<>(operators);

    EvolutionEngine<Genepool> engine
        = new GenerationalEvolutionEngine<>(
        candidateFactory,
        pipeline,
        new SystemEvaluator(),
        new TruncationSelection(0.5),
        new MersenneTwisterRNG());
    engine.addEvolutionObserver(getEvolutionObserver(saver));
    int targetFitness = properties.getTargetFitness();
    int concurrent = properties.getConcurrent();
    int elite = properties.getElites();
    log.info("Starting evolution process with target fitness " + nl + targetFitness + "." + nl +
        "Concurrent organisms: " + concurrent + nl + "Elite population: " + elite + nl);

    Genepool winner = null;
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
      try (InputStream stream = new FileInputStream(propertiesFile)){
        props.load(stream);
      }

      File savedDir = new File(userDir, props.getProperty("output.dir", "../../saved")).getCanonicalFile();

      properties = new EvolutionProperties(
          Integer.parseInt(props.getProperty("elites", "2")),
          Integer.parseInt(props.getProperty("concurrent", "10")),
          Integer.parseInt(props.getProperty("target.fitness", "350000")),
          savedDir
      );
    } catch (IOException e) {
      log.error("Can't read properties " + name, e);
      System.exit(-1);
    }

    return properties;
  }

  private static GenepoolProvider getProvider() {
    return new GeneratorGenepoolProvider(new MersenneTwisterRNG(), Constants.DIMENSION_X, Constants.DIMENSION_Y);
  }

  public static EvolutionObserver<? super Genepool> getEvolutionObserver(SerializationUtil saver) {
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
  private static ArrayList<EvolutionaryOperator<Genepool>> getEvolutionaryOperators() {
    ArrayList<EvolutionaryOperator<Genepool>> operators = new ArrayList<>();
//    ListCrossover e = new ListCrossover(3, new Probability(0.1));
    operators.add(new Mutation());
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

  public static File getSavingDir() {
    try {
      File file = new File(System.getProperty("user.dir"));
      File saved = new File(file, "../../saved").getCanonicalFile();
      if (!saved.exists()) {
        saved.mkdirs();
      }

      log.info("file " + file.getCanonicalPath() + " ");
      return saved;
    } catch (IOException e) {
      log.error("fatal error: ", e);
      System.exit(-1);
      return null;
    }

  }

  private static class EvolutionProperties {
    private final int elites;
    private final int concurrent;
    private final int targetFitness;
    private final File savingDir;

    public EvolutionProperties(int elites, int concurrent, int targetFitness, File savingDir) {
      this.elites = elites;
      this.concurrent = concurrent;
      this.targetFitness = targetFitness;
      this.savingDir = savingDir;
    }

    public int getElites() {
      return elites;
    }

    public int getConcurrent() {
      return concurrent;
    }

    public int getTargetFitness() {
      return targetFitness;
    }

    public File getSavingDir() {
      return savingDir;
    }
  }
}
