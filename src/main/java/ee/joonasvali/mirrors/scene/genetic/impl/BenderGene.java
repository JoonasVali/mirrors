package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Bender;
import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;

import java.util.Collections;
import java.util.List;


public class BenderGene implements Gene<BenderGene> {

  private final double x;
  private final double y;
  private final double radius;
  private final double delta;

  public BenderGene(double x, double y, double radius, double delta) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.delta = delta;
  }

  @Override
  public Gene<BenderGene> copy() {
    return new BenderGene(x, y, radius, delta);
  }

  @Override
  public BenderGene getOffspringGene(GeneFactory geneFactory) {
    return new BenderGene(
        geneFactory.largeRandom(20) + x,
        geneFactory.largeRandom(20) + y,
        geneFactory.largeRandom(10) + radius,
        geneFactory.smallrandom(5) +  delta);
  }

  @Override
  public List<Physical> createPhysicals(Environment environment) {
    return Collections.singletonList(
        new Bender(x, y, 0, radius, delta)
    );
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Environment environment) {
    return null;
  }
}
