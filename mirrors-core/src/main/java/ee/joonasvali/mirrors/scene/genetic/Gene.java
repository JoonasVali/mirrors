package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;

import java.util.List;


public interface Gene<T extends Gene> {
  Gene<T> copy();

  T getOffspringGene(GeneFactory geneFactory);

  List<Physical> createPhysicals(Model model);

  List<LinePhysical> createLinePhysicals(Model model);
}
