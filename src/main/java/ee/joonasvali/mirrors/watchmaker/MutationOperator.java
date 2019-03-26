package ee.joonasvali.mirrors.watchmaker;

import ee.joonasvali.mirrors.scene.Constants;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.impl.LightGoalGene;
import ee.joonasvali.mirrors.scene.genetic.impl.LightProviderGene;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MutationOperator implements EvolutionaryOperator<Genepool> {

  private final double additionRate;
  private final double removalRate;
  private final GeneFactory geneFactory;
  private final IdentityHashMap<Object, Object> a = new IdentityHashMap<>();

  public MutationOperator(GeneFactory geneFactory, double additionRate, double removalRate) {
    this.additionRate = additionRate;
    this.removalRate = removalRate;
    this.geneFactory = geneFactory;
  }

  @Override
  public List<Genepool> apply(List<Genepool> selectedCandidates, Random rng) {
    List<Genepool> result = selectedCandidates.stream().map(Genepool::copy).collect(Collectors.toList());

    List<Genepool> list = new ArrayList<>();
    for (Genepool pool : result) {
      pool.removeIf(current -> canRemove(current) && rng.nextDouble() < removalRate);
      if(rng.nextDouble() < additionRate){
        pool.add(geneFactory.generateGene(Constants.DIMENSION_X, Constants.DIMENSION_Y));
      }
      list.add(pool.getOffspring(geneFactory));
    }
    return list;
  }

  private boolean canRemove(Gene current) {
    return !(current instanceof LightGoalGene) && !(current instanceof LightProviderGene);
  }


}
