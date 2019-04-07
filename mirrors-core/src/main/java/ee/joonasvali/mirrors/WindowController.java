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
  private static final long ENV_UPDATE_DELAY_MS = 9L;
  private static final int GRAPHICS_UPDATE_DELAY_MS = 30;

  private final ModelController env;
  private volatile GameContainer container;
  private volatile Timer timer;
  private volatile boolean running = true;

  public WindowController(ModelController env) {
    this.env = env;
  }

  public void launch() throws InvocationTargetException, InterruptedException {
    env.init();

    SwingUtilities.invokeAndWait(
        () -> {
          try {
            container = new GameContainer(env);
            container.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            container.setSize(new Dimension(Constants.DIMENSION_X, Constants.DIMENSION_Y));
            container.setVisible(true);
          } catch (Exception e) {
            log.error("Unable to launch Mirrors", e);
          }
        }
    );

    // Make the graphics update.
    ActionListener taskPerformer = evt -> container.repaint();
    timer = new Timer(GRAPHICS_UPDATE_DELAY_MS, taskPerformer);
    timer.start();

    // Make the simulation update.
    while (running) {
      synchronized (env.getModel().getLock()) {
        env.nextStep();
      }
      Thread.sleep(ENV_UPDATE_DELAY_MS);
    }


  }
}
