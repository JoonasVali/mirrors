package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;

import java.util.List;


public interface Gene<T extends Gene> {
  Gene<T> copy();
  T getOffspringGene(GeneFactory geneFactory);
  List<Physical> createPhysicals(Environment environment);
  List<LinePhysical> createLinePhysicals(Environment environment);
}
