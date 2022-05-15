package ee.joonasvali.mirrors.util;

import ee.joonasvali.mirrors.scene.genetic.Genome;

public interface GenomeSelectionChangedListener {
  void changed(Genome genome);
}
