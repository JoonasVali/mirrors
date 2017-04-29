package ee.joonasvali.mirrors.watchmaker;

import ee.joonasvali.mirrors.scene.Constants;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneUtil;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: Joss
 * Date: 3/25/14
 * Time: 7:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mutation implements EvolutionaryOperator<Genepool> {

  public static final double CHANCE_OF_REMOVAL = 0.01;
  public static final double CHANCE_OF_ADDITION = 0.05;


  @Override
  public List<Genepool> apply(List<Genepool> selectedCandidates, Random rng) {
    List<Genepool> list = new ArrayList<>();
    for (Genepool pool : selectedCandidates) {
      Iterator<Gene> it = pool.iterator();
      while(it.hasNext()) {
        it.next();
        if(rng.nextDouble() < CHANCE_OF_REMOVAL){

          it.remove();
        }
      }
      if(rng.nextDouble() < CHANCE_OF_ADDITION){
        pool.add(GeneUtil.generateGene(rng, Constants.DIMENSION_X, Constants.DIMENSION_Y));
      }
      list.add(pool.getOffspring(rng));
    }
    return list;
  }


}
