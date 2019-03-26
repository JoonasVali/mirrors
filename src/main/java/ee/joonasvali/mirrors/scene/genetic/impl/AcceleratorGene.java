package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Accelerator;
import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;

import java.util.Collections;
import java.util.List;

public class AcceleratorGene implements Gene<AcceleratorGene> {

  private final double x;
  private final double y;
  private final double radius;
  private final double acceleration;

  public AcceleratorGene(double x, double y, double radius, double acceleration) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.acceleration = acceleration;
  }

  @Override
  public Gene<AcceleratorGene> copy() {
    return new AcceleratorGene(x, y, radius, acceleration);
  }

  @Override
  public AcceleratorGene getOffspringGene(GeneFactory geneFactory) {
    return new AcceleratorGene(
        geneFactory.largeRandom(20) + x,
        geneFactory.largeRandom(20) + y,
        geneFactory.largeRandom(10) + radius,
        geneFactory.smallrandom(20) + acceleration);
  }

  @Override
  public List<Physical> createPhysicals(Environment environment) {
    return Collections.singletonList(new Accelerator(x, y, 0, radius, acceleration));
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Environment environment) {
    return null;
  }
}
