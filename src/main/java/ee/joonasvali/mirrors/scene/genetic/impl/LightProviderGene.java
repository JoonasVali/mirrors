package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.*;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;

import java.util.Collections;
import java.util.List;

public class LightProviderGene implements Gene<LightProviderGene> {
  private final double x;
  private final double y;
  private final double density;
  private final double velocity;
  private final LightGroup group;

  public LightProviderGene(double x, double y, double density, double velocity, LightGroup group) {
    this.x = x;
    this.y = y;
    this.density = density;
    this.velocity = velocity;
    this.group = group;
  }

  @Override
  public Gene<LightProviderGene> copy() {
    return new LightProviderGene(x, y, density, velocity, group);
  }

  @Override
  public LightProviderGene getOffspringGene(GeneFactory geneFactory) {
    return new LightProviderGene(x, y, density, velocity, group);
  }

  @Override
  public List<Physical> createPhysicals(Environment environment) {
    return Collections.singletonList(
        new LightSource(x, y, density, velocity, environment, group)
    );
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Environment environment) {
    return null;
  }

}
