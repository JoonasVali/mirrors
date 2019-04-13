package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;

import java.util.List;


public interface Gene {
  Gene copy();

  Gene getOffspringGene(GeneFactory geneFactory);

  List<Physical> createPhysicals(Model model);

  List<LinePhysical> createLinePhysicals(Model model);
}
