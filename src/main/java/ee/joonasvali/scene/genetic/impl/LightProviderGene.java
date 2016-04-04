package ee.joonasvali.scene.genetic.impl;

import ee.joonasvali.scene.Environment;
import ee.joonasvali.scene.LightEmitterProperties;
import ee.joonasvali.scene.LightSource;
import ee.joonasvali.scene.Physical;
import ee.joonasvali.scene.genetic.Gene;

import java.util.Random;

public class LightProviderGene implements Gene {
  private double x;
  private double y;

  private double density;
  private double velocity;

  public LightProviderGene(double x, double y, double density, double velocity) {
    this.x = x;
    this.y = y;
    this.density = density;
    this.velocity = velocity;
  }

  @Override
  public Physical createPhysical(Environment environment) {
    return new LightSource(x, y, new LightEmitterProperties() {

      @Override
      public double getDensity() {
        return density;
      }

      @Override
      public double getVelocity() {
        return velocity;
      }
    }, environment);
  }

  @Override
  public Gene getOffspringGene(Random random) {
    return new LightProviderGene(x, y, density, velocity);
  }

}
