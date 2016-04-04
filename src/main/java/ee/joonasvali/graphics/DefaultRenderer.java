package ee.joonasvali.graphics;

import ee.joonasvali.scene.Environment;
import ee.joonasvali.scene.Physical;
import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

/**
 * @author Joonas Vali April 2016
 */
public class DefaultRenderer implements Renderer {
  @Override
  public void render(Environment environment, Graphics g) {
    for(Physical object : environment.getObjects()) {
      object.render(g);
    }

    g.setColor(Color.darkGray);
    g.drawString(String.format("%.1f", environment.getScore()), 10, 30);
    g.setColor(Color.darkGray);
  }
}
