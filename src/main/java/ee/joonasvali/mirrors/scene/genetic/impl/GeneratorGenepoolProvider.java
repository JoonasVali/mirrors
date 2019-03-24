package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.LightEmitterProperties;
import ee.joonasvali.mirrors.scene.LightGroup;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.GenepoolProvider;

import java.awt.*;

public class GeneratorGenepoolProvider implements GenepoolProvider {

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

    LightGroup group1 = new LightGroup(1, new Color(255, 150, 150));
    LightGroup group2 = new LightGroup(2, new Color(150, 255, 150));
    LightGroup group3 = new LightGroup(3, new Color(150, 150, 255));
    genepool.add(new LightGoalGene(20, 550, 300));
    genepool.add(createLightEmitter(150, 250, 0.1, 0.8, group1));
    genepool.add(createLightEmitter(150, 300, 0.1, 1, group2));
    genepool.add(createLightEmitter(150, 350, 0.1, 1.2, group3));
    int genes = (int) geneFactory.getRandom(3, 25);
    for (int i = 0; i < genes; i++) {
      Gene gene = generateGene();
      genepool.add(gene);
    }
    return genepool;
  }

  private LightProviderGene createLightEmitter(int x, int y, double density, double velocity, LightGroup group) {
    LightEmitterProperties properties = new LightEmitterProperties() {
      @Override
      public double getDensity() {
        return density;
      }

      @Override
      public double getVelocity() {
        return velocity;
      }
    };
    return new LightProviderGene(x, y, properties, group);
  }

  public Gene generateGene() {
    return geneFactory.generateGene(maxX, maxY);
  }

  @Override
  public Genepool provide() {
    return generate();
  }

}
