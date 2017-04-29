package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Accelerator;
import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneUtil;

import java.util.Random;

public class AcceleratorGene implements Gene {

  private double x;
  private double y;
  private double radius;
  private double acceleration;

  public AcceleratorGene(double x, double y, double radius, double acceleration) {
    this.x = x;
    this.y = y;
    this.radius = radius;
    this.acceleration = acceleration;
  }

  @Override
  public Physical createPhysical(Environment environment) {
    return new Accelerator(x, y, 0, radius, acceleration);
  }

  @Override
  public Gene getOffspringGene(Random random) {
    return new AcceleratorGene(
        GeneUtil.largeRandom(random, 20) + x,
        GeneUtil.largeRandom(random, 20) +  y,
        GeneUtil.largeRandom(random, 10) +  radius,
        GeneUtil.smallrandom(random, 20) +  acceleration);
  }
}
