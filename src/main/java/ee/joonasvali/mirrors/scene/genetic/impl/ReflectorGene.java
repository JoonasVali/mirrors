package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;

import java.util.ArrayList;
import java.util.List;


public class ReflectorGene implements Gene<ReflectorGene> {

  private final double x;
  private final double y;
  private final double width;
  private final double height;

  public ReflectorGene(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public ReflectorGene getOffspringGene(GeneFactory geneFactory) {
    return new ReflectorGene(
        geneFactory.largeRandom(20) + x,
        geneFactory.largeRandom(20) + y,
        geneFactory.largeRandom(20) + width,
        geneFactory.largeRandom(20) + height);
  }

  @Override
  public List<Physical> createPhysicals(Environment environment) {
    return null;
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Environment environment) {
    ArrayList<LinePhysical> physicalArrayList = new ArrayList<>();
    physicalArrayList.add(new LinePhysical(x, y, x + width, y));
    physicalArrayList.add(new LinePhysical(x, y, x, y + height));
    physicalArrayList.add(new LinePhysical(x + width, y, x + width, y + height));
    physicalArrayList.add(new LinePhysical(x, y + height, x + width, y + height));
    return physicalArrayList;
  }
}
