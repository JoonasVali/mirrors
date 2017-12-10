package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;

import java.io.Serializable;
import java.util.List;


public interface Gene<T extends Gene> extends Serializable{
  T getOffspringGene(GeneFactory geneFactory);
  List<Physical> createPhysicals(Environment environment);
  List<LinePhysical> createLinePhysicals(Environment environment);
}
