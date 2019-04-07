package ee.joonasvali.mirrors.watchmaker;

import ee.joonasvali.mirrors.scene.genetic.Genome;
import ee.joonasvali.mirrors.scene.genetic.GenomeProvider;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.Random;


public class GenomeCanditateFactory extends AbstractCandidateFactory<Genome> {

  private GenomeProvider provider;

  public GenomeCanditateFactory(GenomeProvider provider) {
    this.provider = provider;
  }

  @Override
  public Genome generateRandomCandidate(Random rng) {
    return provider.provide();
  }

}
