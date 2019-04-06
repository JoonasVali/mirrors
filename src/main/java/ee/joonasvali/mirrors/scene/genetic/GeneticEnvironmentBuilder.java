package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;

import java.util.List;

public class GeneticEnvironmentBuilder implements EnvironmentBuilder {

  private Genome genome;
  private GenomeProvider provider;

  public GeneticEnvironmentBuilder(GenomeProvider provider) {
    this.provider = provider;
    loadGenome();
  }

  public GeneticEnvironmentBuilder(Genome genome) {
    this.genome = genome;
  }

  @Override
  public Environment getEnvironment() {
    Environment environment = new Environment();
    for (Gene gene : genome.getGenes()) {
      List<Physical> physicalList = gene.createPhysicals(environment);
      if (physicalList != null) {
        physicalList.forEach(environment::addObject);
      }

      List<LinePhysical> linePhysicalList = gene.createLinePhysicals(environment);
      if (linePhysicalList != null) {
        linePhysicalList.forEach(environment::addObject);
      }
    }
    return environment;
  }

  public void loadGenome() {
    if (provider != null) {
      genome = provider.provide();
    }
  }
}
