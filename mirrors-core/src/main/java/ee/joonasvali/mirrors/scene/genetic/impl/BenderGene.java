package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Bender;
import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.genetic.Gene;


public class BenderGene implements Gene {

  public final double x;
  public final double y;
  public final double radius;
  public final double strength;

  public BenderGene(double x, double y, double radius, double strength) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.strength = strength;
  }

  @Override
  public BenderGene copy() {
    return new BenderGene(x, y, radius, strength);
  }

  @Override
  public void expressTo(Model model) {
    model.addObject(new Bender(x, y, 0, radius, strength));
  }
}
