package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.ParticleGroup;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import ee.joonasvali.mirrors.scene.genetic.GenomeProvider;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class GeneratorGenomeProvider implements GenomeProvider {

  private final GeneFactory geneFactory;
  private final double maxX;
  private final double maxY;
  private final boolean topProducer;
  private final boolean middleProducer;
  private final boolean bottomProducer;

  public GeneratorGenomeProvider(GeneFactory geneFactory, double maxX, double maxY,
                                 boolean topProducer, boolean middleProducer, boolean bottomProducer) {
    this.geneFactory = geneFactory;
    this.maxX = maxX;
    this.maxY = maxY;
    this.topProducer = topProducer;
    this.middleProducer = middleProducer;
    this.bottomProducer = bottomProducer;
  }

  private Genome generate() {
    Genome genome = new Genome();

    Set<ParticleGroup> groups = new HashSet<>();

    if (topProducer) {
      ParticleGroup group1 = new ParticleGroup(1, new Color(255, 150, 150));
      genome.add(new ParticleGoalGene(20, 550, 250, new Color(255, 150, 150), group1));
      genome.add(createParticleEmitter(150, 250, 0.1, 0.8, group1));
      groups.add(group1);
    }

    if (middleProducer) {
      ParticleGroup group2 = new ParticleGroup(2, new Color(150, 255, 150));
      genome.add(new ParticleGoalGene(20, 550, 300, new Color(150, 255, 150), group2));
      genome.add(createParticleEmitter(150, 300, 0.1, 1, group2));
      groups.add(group2);
    }

    if (bottomProducer) {
      ParticleGroup group3 = new ParticleGroup(3, new Color(150, 150, 255));
      genome.add(new ParticleGoalGene(20, 550, 350, new Color(150, 150, 255), group3));
      genome.add(createParticleEmitter(150, 350, 0.1, 1.2, group3));
      groups.add(group3);
    }


    int genes = (int) geneFactory.getRandom(3, 25);
    for (int i = 0; i < genes; i++) {
      Gene gene = generateGene();

      if (gene instanceof TriangleReflectorGene && groups.size() > 1) {
        ((TriangleReflectorGene) gene).setAllGroups(groups);
        ((TriangleReflectorGene) gene).setReflectiveGroups(geneFactory.randomSubCollection(groups, 0.20));
      }

      genome.add(gene);
    }
    return genome;
  }

  private ParticleProviderGene createParticleEmitter(int x, int y, double density, double velocity, ParticleGroup group) {
    return new ParticleProviderGene(x, y, density, velocity, group);
  }

  public Gene generateGene() {
    return geneFactory.generateGene(maxX, maxY);
  }

  @Override
  public Genome provide() {
    return generate();
  }

}
