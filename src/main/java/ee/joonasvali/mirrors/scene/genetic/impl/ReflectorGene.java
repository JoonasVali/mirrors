package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.Reflector;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneUtil;

import java.util.Random;


public class ReflectorGene implements Gene {

  private double x;
  private double y;
  private double width;
  private double height;

  public ReflectorGene(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public Physical createPhysical(Environment environment) {
    return new Reflector(x, y, 0, width, height);
  }

  @Override
  public Gene getOffspringGene(Random random) {
    return new ReflectorGene(
        GeneUtil.largeRandom(random, 20) + x,
        GeneUtil.largeRandom(random, 20) + y,
        GeneUtil.largeRandom(random, 20) + width,
        GeneUtil.largeRandom(random, 20) + height);
  }
}
