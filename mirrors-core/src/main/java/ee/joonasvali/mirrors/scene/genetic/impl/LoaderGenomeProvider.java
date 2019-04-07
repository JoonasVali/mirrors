package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.genetic.util.SerializationUtil;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import ee.joonasvali.mirrors.scene.genetic.GenomeProvider;

import java.io.File;
import java.io.IOException;


public class LoaderGenomeProvider implements GenomeProvider {
  private Genome genome;
  private File file;

  public LoaderGenomeProvider(File file) {
    this.file = file;
    init();
  }

  public LoaderGenomeProvider(Genome provided) {
    genome = provided;
  }

  private void init() {
    try {
      genome = SerializationUtil.deserialize(file.toPath());
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public Genome provide() {
    return genome;
  }
}
