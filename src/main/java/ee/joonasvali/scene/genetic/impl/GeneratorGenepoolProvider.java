package ee.joonasvali.scene.genetic.impl;

import ee.joonasvali.scene.genetic.GeneUtil;
import ee.joonasvali.scene.genetic.Gene;
import ee.joonasvali.scene.genetic.Genepool;
import ee.joonasvali.scene.genetic.GenepoolProvider;

import java.util.Random;

public class GeneratorGenepoolProvider implements GenepoolProvider{

  private final Random random;
  private double maxX;
  private double maxY;

  public GeneratorGenepoolProvider(Random random, double maxX, double maxY) {
    this.maxX = maxX;
    this.maxY = maxY;
    this.random = random;
  }

  private Genepool generate() {
    Genepool genepool = new Genepool();
    genepool.add(new LightGoalGene(20, 550, 300));
    genepool.add(new LightProviderGene(150, 300, 0.1, 1));
    int genes = (int) getRandom(3, 25);
    for(int i = 0; i < genes; i++){
      Gene gene = generateGene();
      genepool.add(gene);
    }
    return genepool;
  }

  public Gene generateGene() {
    return GeneUtil.generateGene(random, maxX, maxY);
  }

  @Override
  public Genepool provide() {
    return generate();
  }

  public double getRandom(double min, double max) {
    return min + random.nextDouble() * (max - min);
  }

}
