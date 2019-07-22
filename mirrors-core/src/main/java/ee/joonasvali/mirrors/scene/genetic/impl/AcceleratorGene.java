package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Accelerator;
import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.genetic.Gene;

public class AcceleratorGene implements Gene {

  public final double x;
  public final double y;
  public final double radius;
  public final double acceleration;

  public AcceleratorGene(double x, double y, double radius, double acceleration) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.acceleration = acceleration;
  }

  @Override
  public AcceleratorGene copy() {
    return new AcceleratorGene(x, y, radius, acceleration);
  }

  @Override
  public void expressTo(Model model) {
    model.addObject(new Accelerator(x, y, 0, radius, acceleration));
  }
}
