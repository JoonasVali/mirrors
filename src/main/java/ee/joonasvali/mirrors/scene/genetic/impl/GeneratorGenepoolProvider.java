package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.GenepoolProvider;

public class GeneratorGenepoolProvider implements GenepoolProvider{

  private final GeneFactory geneFactory;
  private final double maxX;
  private final double maxY;

  public GeneratorGenepoolProvider(GeneFactory geneFactory, double maxX, double maxY) {
    this.geneFactory = geneFactory;
    this.maxX = maxX;
    this.maxY = maxY;
  }

  private Genepool generate() {
    Genepool genepool = new Genepool();
    genepool.add(new LightGoalGene(20, 550, 300));
    genepool.add(new LightProviderGene(150, 300, 0.1, 1));
    int genes = (int) geneFactory.getRandom(3, 25);
    for(int i = 0; i < genes; i++){
      Gene gene = generateGene();
      genepool.add(gene);
    }
    return genepool;
  }

  public Gene generateGene() {
    return geneFactory.generateGene(maxX, maxY);
  }

  @Override
  public Genepool provide() {
    return generate();
  }

}
