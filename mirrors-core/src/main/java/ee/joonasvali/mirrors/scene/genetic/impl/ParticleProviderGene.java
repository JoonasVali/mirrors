package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ParticleGroup;
import ee.joonasvali.mirrors.scene.ParticleSource;
import ee.joonasvali.mirrors.scene.genetic.Gene;

public class ParticleProviderGene implements Gene {
  private final double x;
  private final double y;
  private final double density;
  private final double velocity;
  private final ParticleGroup group;

  public ParticleProviderGene(double x, double y, double density, double velocity, ParticleGroup group) {
    this.x = x;
    this.y = y;
    this.density = density;
    this.velocity = velocity;
    this.group = group;
  }

  @Override
  public ParticleProviderGene copy() {
    return new ParticleProviderGene(x, y, density, velocity, group);
  }

  @Override
  public void expressTo(Model model) {
    model.addObject(new ParticleSource(x, y, density, velocity, model, group));
  }

}
