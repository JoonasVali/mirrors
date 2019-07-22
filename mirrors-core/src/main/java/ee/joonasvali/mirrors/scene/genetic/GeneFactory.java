package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.EvolutionProperties;
import ee.joonasvali.mirrors.scene.ParticleGroup;
import ee.joonasvali.mirrors.scene.Repellent;
import ee.joonasvali.mirrors.scene.genetic.impl.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;


public class GeneFactory {
  private final List<GeneProvider> geneProviderList = new ArrayList<>();
  private final double chanceOfMutation;
  private final Random random;
  private Set<ParticleGroup> reflectiveParticleGroups;

  public GeneFactory(EvolutionProperties props, Random random) {
    this.random = random;
    chanceOfMutation = props.getMutationRate();
    if (props.isBendersEnabled()) {
      geneProviderList.add((maxX, maxY) -> {
        double x = getRandom(0, maxX);
        double y = getRandom(0, maxY);
        double radius = getRandom(10, 30);
        double delta = getRandom(0, 4) - 2;
        return new BenderGene(x, y, radius, delta);
      });
    }

    if (props.isReflectorsEnabled()) {
      geneProviderList.add((maxX, maxY) -> {
        double x = getRandom(0, maxX);
        double y = getRandom(0, maxY);
        double x1 = getRandom(10, 150);
        double x2 = getRandom(10, 150);
        double x3 = getRandom(10, 150);
        double y1 = getRandom(10, 150);
        double y2 = getRandom(10, 150);
        double y3 = getRandom(10, 150);
        return new TriangleReflectorGene(x, y, x1, y1, x2, y2, x3, y3);
      });
    }

    if (props.isRepellentsEnabled()) {
      geneProviderList.add((maxX, maxY) -> {
        double x = getRandom(0, maxX);
        double y = getRandom(0, maxY);
        double radius = getRandom(15, 50);
        double delta = getRandom(0.005, 0.1);
        return new RepellentGene(x, y, radius, delta);
      });
    }

    if (props.isAcceleratorsEnabled()) {
      geneProviderList.add((maxX, maxY) -> {
        double x = getRandom(0, maxX);
        double y = getRandom(0, maxY);
        double radius = getRandom(10, 30);
        double acceleration = random.nextDouble() / 10 - 0.05;
        return new AcceleratorGene(x, y, radius, acceleration);
      });
    }
  }

  public Gene generateGene(double maxX, double maxY) {
    GeneFactory.GeneProvider provider = geneProviderList.get(random.nextInt(geneProviderList.size()));
    return provider.createGene(maxX, maxY);
  }

  public double random() {
    if (random.nextDouble() > chanceOfMutation) return 0;
    return 0.5 - random.nextDouble();
  }

  public double largeRandom(double multiplier) {
    if (random.nextDouble() > chanceOfMutation) return 0;
    return (0.5 - random.nextDouble()) * multiplier;
  }

  public double smallrandom(double divider) {
    if (random.nextDouble() > chanceOfMutation) return 0;
    return (0.5 - random.nextDouble()) / divider;
  }

  public double getRandom(double min, double max) {
    return min + random.nextDouble() * (max - min);
  }

  public Set<ParticleGroup> reflectiveGroups(Set<ParticleGroup> reflectiveGroups) {
    Set<ParticleGroup> result = new HashSet<>();
    // Add some elements
    result.addAll(randomSubCollection(reflectiveParticleGroups, 1 - chanceOfMutation));
    // Remove some elements, wi
    result.addAll(randomSubCollection(reflectiveGroups, chanceOfMutation));
    return result;
  }

  public Set<ParticleGroup> randomSubCollection(Set<ParticleGroup> groups, double chanceOfNotIncludingElement) {
    if (chanceOfNotIncludingElement > 1 || chanceOfNotIncludingElement < 0) {
      throw new IllegalArgumentException("Bad chanceOfNotIncludingElement: " + chanceOfNotIncludingElement);
    }

    return groups.stream().filter(
        group -> random.nextDouble() > chanceOfNotIncludingElement
    ).collect(Collectors.toSet());
  }

  public void setReflectiveParticleGroups(Set<ParticleGroup> groups) {
    reflectiveParticleGroups = groups;
  }

  private interface GeneProvider {
    Gene createGene(double maxX, double maxY);
  }

  Gene getOffspringGene(Gene gene) {
    if (gene instanceof AcceleratorGene) {
      AcceleratorGene accGene = (AcceleratorGene) gene;
      return new AcceleratorGene(
          largeRandom(20) + accGene.x,
          largeRandom(20) + accGene.y,
          largeRandom(10) + accGene.radius,
          smallrandom(20) + accGene.acceleration);
    }

    if (gene instanceof BenderGene) {
      BenderGene benderGene = (BenderGene) gene;
      return new BenderGene(
          largeRandom(20) + benderGene.x,
          largeRandom(20) + benderGene.y,
          largeRandom(10) + benderGene.radius,
          smallrandom(5) + benderGene.strength);
    }

    if (gene instanceof RepellentGene) {
      RepellentGene repellentGene = (RepellentGene) gene;
      return new RepellentGene(
          largeRandom(20) + repellentGene.x,
          largeRandom(20) + repellentGene.y,
          largeRandom(10) + repellentGene.radius,
          smallrandom(50) + repellentGene.delta);
    }

    if (gene instanceof TriangleReflectorGene) {
      TriangleReflectorGene reflGene = (TriangleReflectorGene) gene;
      TriangleReflectorGene newGene = new TriangleReflectorGene(
          largeRandom(20) + reflGene.x,
          largeRandom(20) + reflGene.y,
          largeRandom(5) + reflGene.x1,
          largeRandom(5) + reflGene.y1,
          largeRandom(5) + reflGene.x2,
          largeRandom(5) + reflGene.y2,
          largeRandom(5) + reflGene.x3,
          largeRandom(5) + reflGene.y3
      );
      if (reflGene.getReflectiveGroups() != null) {
        newGene.setReflectiveGroups(reflectiveGroups(reflGene.getReflectiveGroups()));
      }
      return newGene;
    }

    if (gene instanceof ParticleGoalGene) {
      return gene.copy();
    }

    if (gene instanceof ParticleProviderGene) {
      return gene.copy();
    }

    throw new IllegalArgumentException("No offspring handler for " + gene.getClass());
  }
}
