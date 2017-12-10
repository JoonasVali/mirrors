package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.Repellent;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;

import java.util.Collections;
import java.util.List;

public class RepellentGene implements Gene<RepellentGene> {

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
  public RepellentGene getOffspringGene(GeneFactory geneFactory) {
    return new RepellentGene(
        geneFactory.largeRandom(20) + x,
        geneFactory.largeRandom(20) + y,
        geneFactory.largeRandom(10) + radius,
        geneFactory.smallrandom(50) +  delta);
  }

  @Override
  public List<Physical> createPhysicals(Environment environment) {
    return Collections.singletonList(
        new Repellent(x, y, 0, radius, delta)
    );
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Environment environment) {
    return null;
  }
}
