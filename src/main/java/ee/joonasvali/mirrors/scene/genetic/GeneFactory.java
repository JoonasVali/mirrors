package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.EvolutionProperties;
import ee.joonasvali.mirrors.scene.genetic.impl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GeneFactory {
  private final List<GeneProvider> geneProviderList = new ArrayList<>();
  private final double chanceOfMutation;
  private final Random random;

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
        double x1 = getRandom(10, 40);
        double x2 = getRandom(10, 40);
        double x3 = getRandom(10, 40);
        double y1 = getRandom(10, 40);
        double y2 = getRandom(10, 40);
        double y3 = getRandom(10, 40);
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

  private interface GeneProvider {
    Gene createGene(double maxX, double maxY);
  }
}
