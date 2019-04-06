package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ModelBuilder;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;

import java.util.List;

public class GeneticModelBuilder implements ModelBuilder {

  private Genome genome;
  private GenomeProvider provider;

  public GeneticModelBuilder(GenomeProvider provider) {
    this.provider = provider;
    loadGenome();
  }

  public GeneticModelBuilder(Genome genome) {
    this.genome = genome;
  }

  @Override
  public Model buildModel() {
    Model model = new Model();
    for (Gene gene : genome.getGenes()) {
      List<Physical> physicalList = gene.createPhysicals(model);
      if (physicalList != null) {
        physicalList.forEach(model::addObject);
      }

      List<LinePhysical> linePhysicalList = gene.createLinePhysicals(model);
      if (linePhysicalList != null) {
        linePhysicalList.forEach(model::addObject);
      }
    }
    return model;
  }

  public void loadGenome() {
    if (provider != null) {
      genome = provider.provide();
    }
  }
}
