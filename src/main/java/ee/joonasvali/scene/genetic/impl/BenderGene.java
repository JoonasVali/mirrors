package ee.joonasvali.scene.genetic.impl;

import ee.joonasvali.scene.Bender;
import ee.joonasvali.scene.Environment;
import ee.joonasvali.scene.genetic.GeneUtil;
import ee.joonasvali.scene.Physical;
import ee.joonasvali.scene.genetic.Gene;

import java.util.Random;


public class BenderGene implements Gene {

  private double x;
  private double y;
  private double radius;
  private double delta;

  public BenderGene(double x, double y, double radius, double delta) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.delta = delta;
  }

  @Override
  public Physical createPhysical(Environment environment) {
    return new Bender(x, y, 0, radius, delta);
  }

  @Override
  public Gene getOffspringGene(Random random) {
    return new BenderGene(
        GeneUtil.largeRandom(random, 20) + x,
        GeneUtil.largeRandom(random, 20) + y,
        GeneUtil.largeRandom(random, 10) + radius,
        GeneUtil.smallrandom(random, 5) +  delta);
  }
}
