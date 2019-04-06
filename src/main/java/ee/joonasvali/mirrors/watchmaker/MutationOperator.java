package ee.joonasvali.mirrors.watchmaker;

import ee.joonasvali.mirrors.scene.Constants;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import ee.joonasvali.mirrors.scene.genetic.impl.ParticleGoalGene;
import ee.joonasvali.mirrors.scene.genetic.impl.ParticleProviderGene;
import org.uncommons.watchmaker.framework.EvolutionaryOperator;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class MutationOperator implements EvolutionaryOperator<Genome> {

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
  public List<Genome> apply(List<Genome> selectedCandidates, Random rng) {
    List<Genome> result = selectedCandidates.stream().map(Genome::copy).collect(Collectors.toList());

    List<Genome> list = new ArrayList<>();
    for (Genome genome : result) {
      genome.removeIf(current -> canRemove(current) && rng.nextDouble() < removalRate);
      if(rng.nextDouble() < additionRate){
        genome.add(geneFactory.generateGene(Constants.DIMENSION_X, Constants.DIMENSION_Y));
      }
      list.add(genome.getOffspring(geneFactory));
    }
    return list;
  }

  private boolean canRemove(Gene current) {
    return !(current instanceof ParticleGoalGene) && !(current instanceof ParticleProviderGene);
  }


}
