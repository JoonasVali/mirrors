package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LightGoal;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;

import java.util.Random;

public class LightGoalGene implements Gene {
  private double radius;
  private double x;
  private double y;

  public LightGoalGene(double radius, double x, double y) {
    this.radius = radius;
    this.x = x;
    this.y = y;
  }

  @Override
  public Physical createPhysical(Environment environment) {
    return new LightGoal(x - radius / 2, y - radius / 2, 0, radius, light -> {  });
  }

  @Override
  public Gene getOffspringGene(Random random) {
    return new LightGoalGene(radius, x, y);
  }
}
