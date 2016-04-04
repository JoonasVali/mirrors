package ee.joonasvali.scene.genetic;

import ee.joonasvali.scene.Environment;
import ee.joonasvali.scene.Physical;

import java.io.Serializable;
import java.util.Random;


public interface Gene extends Serializable{
  public Physical createPhysical(Environment environment);
  public Gene getOffspringGene(Random random);
}
