package ee.joonasvali.mirrors.scene.label;

import ee.joonasvali.mirrors.scene.Physical;

import java.awt.*;

public class BoxLabelPhysical implements Physical {
  private final int x;
  private final int y;
  private final int width;
  private final int height;
  private final Color color;

  public BoxLabelPhysical(int x, int y, int width, int height, Color color) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
    this.color = color;
  }

  @Override
  public double getX() {
    return x;
  }

  @Override
  public double getY() {
    return y;
  }

  @Override
  public double getRotation() {
    return 0;
  }

  @Override
  public double getWidth() {
    return width;
  }

  @Override
  public double getHeight() {
    return height;
  }

  @Override
  public void render(Graphics2D g) {
    g.setColor(color);
    g.drawRect(x, y, width, height);
  }
}
