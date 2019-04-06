package ee.joonasvali.mirrors.watchmaker;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import ee.joonasvali.mirrors.scene.genetic.GeneticModelBuilder;
import org.slf4j.LoggerFactory;
import org.uncommons.watchmaker.framework.FitnessEvaluator;

import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class SystemEvaluator implements FitnessEvaluator<Genome> {

  private final Map<Genome, Double> cache = new WeakHashMap<>();

  @Override
  public double getFitness(Genome candidate, List<? extends Genome> population) {
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

  private double evaluate(Genome candidate) {
    ModelBuilder builder = new GeneticModelBuilder(candidate);
    Model model = builder.buildModel();
    model.actUntilNoLightLeft();
    LoggerFactory.getLogger(SystemEvaluator.class).info("Evaluated system to " + model.getScore());
    return model.getScore();
  }

  @Override
  public boolean isNatural() {
    return true;
  }
}
