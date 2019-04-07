package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Bender;
import ee.joonasvali.mirrors.scene.Model;
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
  private final double strength;

  public BenderGene(double x, double y, double radius, double strength) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.strength = strength;
  }

  @Override
  public Gene<BenderGene> copy() {
    return new BenderGene(x, y, radius, strength);
  }

  @Override
  public BenderGene getOffspringGene(GeneFactory geneFactory) {
    return new BenderGene(
        geneFactory.largeRandom(20) + x,
        geneFactory.largeRandom(20) + y,
        geneFactory.largeRandom(10) + radius,
        geneFactory.smallrandom(5) + strength);
  }

  @Override
  public List<Physical> createPhysicals(Model model) {
    return Collections.singletonList(
        new Bender(x, y, 0, radius, strength)
    );
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Model model) {
    return null;
  }
}
