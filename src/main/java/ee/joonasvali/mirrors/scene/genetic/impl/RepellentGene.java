package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.Repellent;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneUtil;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RepellentGene implements Gene<RepellentGene> {

  private double x;
  private double y;
  private double radius;
  private double delta;

  public RepellentGene(double x, double y, double radius, double delta) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.delta = delta;
  }

  @Override
  public RepellentGene getOffspringGene(Random random) {
    return new RepellentGene(
        GeneUtil.largeRandom(random, 20) + x,
        GeneUtil.largeRandom(random, 20) + y,
        GeneUtil.largeRandom(random, 10) + radius,
        GeneUtil.smallrandom(random, 50) +  delta);
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
