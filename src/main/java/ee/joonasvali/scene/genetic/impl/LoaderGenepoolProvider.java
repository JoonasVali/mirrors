package ee.joonasvali.scene.genetic.impl;

import ee.joonasvali.scene.genetic.Genepool;
import ee.joonasvali.scene.genetic.GenepoolProvider;

import java.io.File;


public class LoaderGenepoolProvider implements GenepoolProvider {
  private Genepool genepool;
  private File file;

  public LoaderGenepoolProvider(File file) {
    this.file = file;
    init();
  }

  public LoaderGenepoolProvider(Genepool provided) {
    genepool = provided;
  }

  private void init() {
    genepool = SerializationUtil.get(file);
  }

  @Override
  public Genepool provide() {
    return genepool;
  }
}
