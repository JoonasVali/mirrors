package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ModelBuilder;

public class GeneticModelBuilder implements ModelBuilder {

  private Genome genome;
  private GenomeProvider provider;
  private volatile boolean reload;

  public GeneticModelBuilder(GenomeProvider provider) {
    this.provider = provider;
    loadGenome();
  }

  public GeneticModelBuilder(Genome genome) {
    this.genome = genome;
  }

  public void setReload() {
    this.reload = true;
  }

  @Override
  public Model buildModel() {
    if (reload) {
      loadGenome();
      reload = false;
    }
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
