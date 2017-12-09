package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Joonas Vali April 2017
 */
public class TriangleReflectorGene implements Gene<TriangleReflectorGene> {

  private double x;
  private double y;

  private double x1;
  private double y1;
  private double x2;
  private double y2;
  private double x3;
  private double y3;


  public TriangleReflectorGene(double x, double y, double x1, double y1, double x2, double y2, double x3, double y3) {
    this.x1 = x1;
    this.x2 = x2;
    this.x3 = x3;
    this.y1 = y1;
    this.y2 = y2;
    this.y3 = y3;
    this.x = x;
    this.y = y;
  }

  @Override
  public TriangleReflectorGene getOffspringGene(Random random) {
    return new TriangleReflectorGene(
        GeneUtil.largeRandom(random, 20) + x,
        GeneUtil.largeRandom(random, 20) + y,
        GeneUtil.largeRandom(random, 5) + x1,
        GeneUtil.largeRandom(random, 5) + y1,
        GeneUtil.largeRandom(random, 5) + x2,
        GeneUtil.largeRandom(random, 5) + y2,
        GeneUtil.largeRandom(random, 5) + x3,
        GeneUtil.largeRandom(random, 5) + y3
    );
  }

  @Override
  public List<Physical> createPhysicals(Environment environment) {
    return null;
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Environment environment) {
    List<LinePhysical> lines = new ArrayList<>();
    lines.add(new LinePhysical(x1 + x, y1 + y, x2 + x, y2 + y));
    lines.add(new LinePhysical(x2 + x, y2 + y, x3 + x, y3 + y));
    lines.add(new LinePhysical(x3 + x, y3 + y, x1 + x, y1 + y));
    return lines;
  }
}
