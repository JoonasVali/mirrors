package ee.joonasvali.mirrors.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author Joonas Vali April 2017
 */
public class KeepAliveUtil {
  private static Logger log = LoggerFactory.getLogger(KeepAliveUtil.class);
  private static AtomicBoolean running = new AtomicBoolean();
  private static final ExecutorService service = Executors.newFixedThreadPool(1,
      r -> {
        Thread t = Executors.defaultThreadFactory().newThread(r);
        t.setDaemon(true);
        return t;
      }
  );

  public static void keepAlive() {
    if (running.compareAndSet(false, true)) {
      Robot rob;
      try {
        rob = new Robot();
      } catch (AWTException e) {
        log.error("Unable to create robot", e);
        return;
      }

      Robot finalRob = rob;
      service.execute(() -> {
        while (true) {
          try {
            Thread.sleep(180000);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            break;
          }
          Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
          finalRob.mouseMove(mouseLoc.x, mouseLoc.y);
        }
      });
    }
  }
}
