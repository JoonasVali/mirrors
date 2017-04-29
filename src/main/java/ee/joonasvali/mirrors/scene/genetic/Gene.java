package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;

import java.io.Serializable;
import java.util.List;
import java.util.Random;


public interface Gene extends Serializable{
  Gene getOffspringGene(Random random);
  List<Physical> createPhysicals(Environment environment);
  List<LinePhysical> createLinePhysicals(Environment environment);
}
