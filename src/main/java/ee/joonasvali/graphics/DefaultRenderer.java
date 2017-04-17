package ee.joonasvali.graphics;

import ee.joonasvali.scene.Environment;
import ee.joonasvali.scene.Physical;

import java.awt.Color;
import java.awt.Graphics2D;


/**
 * @author Joonas Vali April 2016
 */
public class DefaultRenderer implements Renderer {
  @Override
  public void render(Environment environment, Graphics2D g) {
    synchronized (environment.getLock()) {
      g.setColor(Color.BLACK);
      g.fillRect(0, 0, 1000, 1000);
      for (Physical object : environment.getObjects()) {
        object.render(g);
      }
    }

    g.setColor(Color.darkGray);
    g.drawString(String.format("%.1f", environment.getScore()), 10, 30);
    g.setColor(Color.darkGray);
  }
}
