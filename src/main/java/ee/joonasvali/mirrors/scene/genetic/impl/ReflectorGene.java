package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class ReflectorGene implements Gene<ReflectorGene> {

  private double x;
  private double y;
  private double width;
  private double height;

  public ReflectorGene(double x, double y, double width, double height) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

  @Override
  public ReflectorGene getOffspringGene(Random random) {
    return new ReflectorGene(
        GeneUtil.largeRandom(random, 20) + x,
        GeneUtil.largeRandom(random, 20) + y,
        GeneUtil.largeRandom(random, 20) + width,
        GeneUtil.largeRandom(random, 20) + height);
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
