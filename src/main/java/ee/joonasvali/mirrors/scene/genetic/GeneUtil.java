package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.genetic.impl.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class GeneUtil {
  private static final GeneUtil instance = new GeneUtil();
  private final List<GeneProvider> geneProviderList = new ArrayList<>();

  private GeneUtil() {
    geneProviderList.add((rng, maxX, maxY) -> {
      double x = getRandom(rng, 0, maxX);
      double y = getRandom(rng, 0, maxY);
      double radius = getRandom(rng, 10, 30);
      double delta = getRandom(rng, 0, 4) - 2;
      return new BenderGene(x, y, radius, delta);
    });

    geneProviderList.add((rng, maxX, maxY) -> {
      double x = getRandom(rng, 0, maxX);
      double y = getRandom(rng, 0, maxY);
      double width = getRandom(rng, 10, 30);
      double height = getRandom(rng, 10, 30);
      return new ReflectorGene(x, y, width, height);
    });

    geneProviderList.add((rng, maxX, maxY) -> {
      double x = getRandom(rng, 0, maxX);
      double y = getRandom(rng, 0, maxY);
      double x1 = getRandom(rng, 10, 40);
      double x2 = getRandom(rng, 10, 40);
      double x3 = getRandom(rng, 10, 40);
      double y1 = getRandom(rng, 10, 40);
      double y2 = getRandom(rng, 10, 40);
      double y3 = getRandom(rng, 10, 40);
      return new TriangleReflectorGene(x, y, x1, y1, x2, y2, x3, y3);
    });

    geneProviderList.add((rng, maxX, maxY) -> {
      double x = getRandom(rng, 0, maxX);
      double y = getRandom(rng, 0, maxY);
      double radius = getRandom(rng, 15, 50);
      double delta = getRandom(rng, 0.005, 0.1);
      return new RepellentGene(x, y, radius, delta);
    });

    geneProviderList.add((rng, maxX, maxY) -> {
      double x = getRandom(rng, 0, maxX);
      double y = getRandom(rng, 0, maxY);
      double radius = getRandom(rng, 10, 30);
      double acceleration = rng.nextDouble() / 10 - 0.05;
      return new AcceleratorGene(x, y, radius, acceleration);
    });
  }

  private Gene generateGeneInstance(Random rng, double maxX, double maxY) {
    GeneProvider provider = geneProviderList.get(rng.nextInt(geneProviderList.size()));
    return provider.createGene(rng, maxX, maxY);
  }

  private static double CHANCE_OF_MUTATION = 0.1;

  public static double random(Random random) {
    if (random.nextDouble() > CHANCE_OF_MUTATION) return 0;
    return 0.5 - random.nextDouble();
  }

  public static double largeRandom(Random random, double multiplier) {
    if (random.nextDouble() > CHANCE_OF_MUTATION) return 0;
    return (0.5 - random.nextDouble()) * multiplier;
  }

  public static double smallrandom(Random random, double divider) {
    if (random.nextDouble() > CHANCE_OF_MUTATION) return 0;
    return (0.5 - random.nextDouble()) / divider;
  }

  public static Gene generateGene(Random rng, double maxX, double maxY) {
    return instance.generateGeneInstance(rng, maxX, maxY);
  }

  public static double getRandom(Random random, double min, double max) {
    return min + random.nextDouble() * (max - min);
  }

  private interface GeneProvider {
    Gene createGene(Random rng, double maxX, double maxY);
  }
}
