package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.genetic.util.SerializationUtil;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.GenepoolProvider;

import java.io.File;
import java.io.IOException;


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
    try {
      genepool = SerializationUtil.deserialize(file.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Genepool provide() {
    return genepool;
  }
}
