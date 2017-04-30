package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.genetic.impl.AcceleratorGene;
import ee.joonasvali.mirrors.scene.genetic.impl.BenderGene;
import ee.joonasvali.mirrors.scene.genetic.impl.ReflectorGene;
import ee.joonasvali.mirrors.scene.genetic.impl.TriangleReflectorGene;

import java.util.Random;


public class GeneUtil {
  private static double CHANCE_OF_MUTATION = 0.1;

  public static double random(Random random) {
    if(random.nextDouble() > CHANCE_OF_MUTATION) return 0;
    return 0.5 - random.nextDouble();
  }

  public static double largeRandom(Random random, double multiplier) {
    if(random.nextDouble() > CHANCE_OF_MUTATION) return 0;
    return (0.5 - random.nextDouble()) * multiplier;
  }

  public static double smallrandom(Random random, double divider) {
    if(random.nextDouble() > CHANCE_OF_MUTATION) return 0;
    return (0.5 - random.nextDouble()) / divider;
  }

  public static Gene generateGene(Random rng, double maxX, double maxY) {
    Gene gene;
    int type = (int)(rng.nextDouble() * 4);
    double x = getRandom(rng, 0, maxX);
    double y = getRandom(rng, 0, maxY);
    if(type == 0) {
      double radius = getRandom(rng, 10, 30);
      double delta = getRandom(rng, 0, 4) - 2;
      gene = new BenderGene(x, y, radius, delta);
    } else if(type == 1){
      double width = getRandom(rng, 10, 30);
      double height = getRandom(rng, 10, 30);
      gene = new ReflectorGene(x, y, width, height);
    } else if(type == 2){
      double x1 = getRandom(rng, 10, 40);
      double x2 = getRandom(rng, 10, 40);
      double x3 = getRandom(rng, 10, 40);
      double y1 = getRandom(rng, 10, 40);
      double y2 = getRandom(rng, 10, 40);
      double y3 = getRandom(rng, 10, 40);
      gene = new TriangleReflectorGene(x, y, x1, y1, x2, y2, x3, y3);
    } else {
      double radius = getRandom(rng, 10, 30);
      double acceleration = rng.nextDouble() / 10 - 0.05;
      gene = new AcceleratorGene(x, y, radius, acceleration);
    }
    return gene;
  }

  public static double getRandom(Random random, double min, double max) {
    return min + random.nextDouble() * (max - min);
  }
}
