package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.Repellent;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;

public class RepellentGene implements Gene {

  private final double x;
  private final double y;
  private final double radius;
  private final double delta;

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
  public RepellentGene getOffspringGene(GeneFactory geneFactory) {
    return new RepellentGene(
        geneFactory.largeRandom(20) + x,
        geneFactory.largeRandom(20) + y,
        geneFactory.largeRandom(10) + radius,
        geneFactory.smallrandom(50) + delta);
  }

  @Override
  public void expressTo(Model model) {
    model.addObject(
        new Repellent(x, y, 0, radius, delta)
    );
  }
}
