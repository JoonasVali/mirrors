package ee.joonasvali.mirrors.scene;

import java.awt.*;
import java.io.Serializable;

public class LightGroup implements Serializable {
  private final int id;
  private Color color;
  // Kept with lazy initialization due to exclusion from serialization.
  private volatile transient Cache cache;

  public LightGroup(int id, Color color) {
    this.id = id;
    this.color = color;
  }

  public void render(Graphics2D g, double x, double y, double intensity, double initialIntensity) {
    if (cache == null) {
      cache = new Cache(color);
    }

    if (intensity < 0) {
      return;
    }
    int rate = (int) (intensity / initialIntensity * 255);
    g.setColor(cache.colors[rate]);

    g.drawLine((int)x, (int)y, (int)x, (int)y);
  }

  public Color getColor() {
    return color;
  }

  public int getId() {
    return id;
  }

  private static class Cache {
    private transient Color[] colors = new Color[256];
    private Cache(Color color) {
      for (int i = 0; i < colors.length; i++) {
        colors[i] = new Color(color.getRed(), color.getGreen(), color.getBlue(), i);
      }
    }
  }
}
