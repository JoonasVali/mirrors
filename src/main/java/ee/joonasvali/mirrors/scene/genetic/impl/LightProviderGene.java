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

  public LightProviderGene(double x, double y, double density, double velocity) {
    this.x = x;
    this.y = y;
    this.density = density;
    this.velocity = velocity;
  }

  @Override
  public LightProviderGene getOffspringGene(GeneFactory geneFactory) {
    return new LightProviderGene(x, y, density, velocity);
  }

  @Override
  public List<Physical> createPhysicals(Environment environment) {
    return Collections.singletonList(
        new LightSource(x, y, new LightEmitterProperties() {
          @Override
          public double getDensity() {
            return density;
          }

          @Override
          public double getVelocity() {
            return velocity;
          }
        }, environment)
    );
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Environment environment) {
    return null;
  }

}
