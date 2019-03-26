package ee.joonasvali.mirrors.watchmaker;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.EnvironmentBuilder;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.GeneticEnvironmentBuilder;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

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
    LoggerFactory.getLogger(SystemEvaluator.class).info("Evaluated system to " + environment.getScore());
    return environment.getScore();
  }

  @Override
  public boolean isNatural() {
    return true;
  }
}
