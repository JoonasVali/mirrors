package ee.joonasvali.mirrors.watchmaker;

import ee.joonasvali.mirrors.scene.Constants;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class MutationOperator implements EvolutionaryOperator<Genepool> {

  private final double additionRate;
  private final double removalRate;
  private final GeneFactory geneFactory;

  public MutationOperator(GeneFactory geneFactory, double additionRate, double removalRate) {
    this.additionRate = additionRate;
    this.removalRate = removalRate;
    this.geneFactory = geneFactory;
  }

  @Override
  public List<Genepool> apply(List<Genepool> selectedCandidates, Random rng) {
    List<Genepool> list = new ArrayList<>();
    for (Genepool pool : selectedCandidates) {
      Iterator<Gene> it = pool.iterator();
      while(it.hasNext()) {
        it.next();
        if(rng.nextDouble() < removalRate){
          it.remove();
        }
      }
      if(rng.nextDouble() < additionRate){
        pool.add(geneFactory.generateGene(Constants.DIMENSION_X, Constants.DIMENSION_Y));
      }
      list.add(pool.getOffspring(geneFactory));
    }
    return list;
  }


}
