package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LightGoal;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LightGoalGene implements Gene<LightGoalGene> {
  private double radius;
  private double x;
  private double y;

  public LightGoalGene(double radius, double x, double y) {
    this.radius = radius;
    this.x = x;
    this.y = y;
  }

  @Override
  public LightGoalGene getOffspringGene(Random random) {
    return new LightGoalGene(radius, x, y);
  }

  @Override
  public List<Physical> createPhysicals(Environment environment) {
    return Collections.singletonList(new LightGoal(x - radius / 2, y - radius / 2, 0, radius, light -> {  }));
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Environment environment) {
    return null;
  }
}
