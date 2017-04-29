package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;

import java.util.List;

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

  public void loadGenepool(){
    if(provider != null)
      genepool = provider.provide();
  }
}
