package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LightGoal;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;

import java.util.Collections;
import java.util.List;

public class LightGoalGene implements Gene<LightGoalGene> {
  private final double radius;
  private final double x;
  private final double y;

  public LightGoalGene(double radius, double x, double y) {
    this.radius = radius;
    this.x = x;
    this.y = y;
  }

  @Override
  public LightGoalGene getOffspringGene(GeneFactory geneFactory) {
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
