package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.*;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;

import java.util.Collections;
import java.util.List;

public class LightProviderGene implements Gene<LightProviderGene> {
  private final double x;
  private final double y;

  private final LightEmitterProperties properties;

  private final LightGroup group;

  public LightProviderGene(double x, double y, LightEmitterProperties properties, LightGroup group) {
    this.x = x;
    this.y = y;
    this.properties = properties;
    this.group = group;
  }

  @Override
  public LightProviderGene getOffspringGene(GeneFactory geneFactory) {
    return new LightProviderGene(x, y, properties, group);
  }

  @Override
  public List<Physical> createPhysicals(Environment environment) {
    return Collections.singletonList(
        new LightSource(x, y, properties, environment, group)
    );
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Environment environment) {
    return null;
  }

}
