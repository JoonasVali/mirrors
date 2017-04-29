package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.Physical;

import java.io.Serializable;
import java.util.Random;


public interface Gene extends Serializable{
  public Physical createPhysical(Environment environment);
  public Gene getOffspringGene(Random random);
}
