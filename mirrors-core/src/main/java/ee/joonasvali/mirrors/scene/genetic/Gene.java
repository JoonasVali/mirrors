package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Model;


public interface Gene {
  Gene copy();

  Gene getOffspringGene(GeneFactory geneFactory);

  void expressTo(Model model);
}
