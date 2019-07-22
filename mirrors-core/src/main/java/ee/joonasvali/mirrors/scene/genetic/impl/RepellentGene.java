package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.Repellent;
import ee.joonasvali.mirrors.scene.genetic.Gene;

public class RepellentGene implements Gene {

  public final double x;
  public final double y;
  public final double radius;
  public final double delta;

  public RepellentGene(double x, double y, double radius, double delta) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.delta = delta;
  }

  @Override
  public RepellentGene copy() {
    return new RepellentGene(x, y, radius, delta);
  }

  @Override
  public void expressTo(Model model) {
    model.addObject(
        new Repellent(x, y, 0, radius, delta)
    );
  }
}
