package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ModelBuilder;

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
    genome.getGenes().forEach(gene -> gene.expressTo(model));
    return model;
  }

  public void loadGenome() {
    if (provider != null) {
      genome = provider.provide();
    }
  }
}
