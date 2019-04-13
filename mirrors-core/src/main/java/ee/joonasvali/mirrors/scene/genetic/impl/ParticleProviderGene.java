package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.*;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;

import java.util.Collections;
import java.util.List;

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
  public ParticleProviderGene getOffspringGene(GeneFactory geneFactory) {
    return new ParticleProviderGene(x, y, density, velocity, group);
  }

  @Override
  public List<Physical> createPhysicals(Model model) {
    return Collections.singletonList(
        new ParticleSource(x, y, density, velocity, model, group)
    );
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Model model) {
    return null;
  }

}
