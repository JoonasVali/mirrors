package ee.joonasvali.watchmaker;

import ee.joonasvali.scene.Environment;
import ee.joonasvali.scene.EnvironmentBuilder;
import ee.joonasvali.scene.genetic.Genepool;
import ee.joonasvali.scene.genetic.GeneticEnvironmentBuilder;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: Joss
 * Date: 3/25/14
 * Time: 8:01 PM
 * To change this template use File | Settings | File Templates.
 */
public class SystemEvaluator implements FitnessEvaluator<Genepool> {

  private final Map<Genepool, Double> cache = new WeakHashMap<>();
  @Override
  public double getFitness(Genepool candidate, List<? extends Genepool> population) {
    Double score;
    synchronized (cache) {
      score = cache.get(candidate);
    }
    if(score == null){
      score = evaluate(candidate);
      synchronized (cache) {
        cache.put(candidate, score);
      }
    }
    return score;
  }

  private double evaluate(Genepool candidate) {
    EnvironmentBuilder builder = new GeneticEnvironmentBuilder(candidate);
    Environment environment = builder.getEnvironment();
    environment.actUntilNoLightLeft();
    return environment.getScore();
  }

  @Override
  public boolean isNatural() {
    return true;
  }
}
