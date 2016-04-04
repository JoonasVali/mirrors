package ee.joonasvali.scene.genetic;

import ee.joonasvali.scene.Environment;
import ee.joonasvali.scene.EnvironmentBuilder;

public class GeneticEnvironmentBuilder implements EnvironmentBuilder {

  private Genepool genepool;
  private GenepoolProvider provider;

  public GeneticEnvironmentBuilder(GenepoolProvider provider){
    this.provider = provider;
    loadGenepool();
  }

  public GeneticEnvironmentBuilder(Genepool pool){
    this.genepool = pool;
  }

  @Override
  public Environment getEnvironment() {
    Environment environment = new Environment();
    for(Gene gene : genepool.getGenes()) {
      environment.addObject(gene.createPhysical(environment));
    }
    return environment;
  }

  public void loadGenepool(){
    if(provider != null)
      genepool = provider.provide();
  }
}
