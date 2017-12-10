package ee.joonasvali.mirrors;

import ee.joonasvali.mirrors.graphics.GameContainer;
import ee.joonasvali.mirrors.scene.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.lang.reflect.InvocationTargetException;

/**
 * @author Joonas Vali April 2017
 */
public class WindowController {
  private static final Logger log = LoggerFactory.getLogger(Launcher.class);

  private final EnvironmentController env;
  private volatile GameContainer container;
  private volatile Timer timer;
  private volatile boolean running = true;

  public WindowController(EnvironmentController env) {
    this.env = env;
  }

  public void launch() throws InvocationTargetException, InterruptedException {
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
    int delay = 30;
    ActionListener taskPerformer = evt -> container.repaint();
    timer = new Timer(delay, taskPerformer);
    timer.start();

    // Make the simulation update.
    final Object lock = env.getEnvironment().getLock();
    while (running) {

      synchronized (lock) {
        env.nextStep();
      }

      Thread.sleep(9L);
    }


  }
}
