package ee.joonasvali.watchmaker;

import ee.joonasvali.scene.genetic.Genepool;
import ee.joonasvali.scene.genetic.GenepoolProvider;
import org.uncommons.watchmaker.framework.factories.AbstractCandidateFactory;

import java.util.*;


public class GenepoolCanditateFactory extends AbstractCandidateFactory<Genepool> {

  private GenepoolProvider provider;
  public GenepoolCanditateFactory(GenepoolProvider provider) {
    this.provider = provider;
  }

  @Override
  public Genepool generateRandomCandidate(Random rng) {
    return provider.provide();
  }

}
