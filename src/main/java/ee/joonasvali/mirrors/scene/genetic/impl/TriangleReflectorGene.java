package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Environment;
import ee.joonasvali.mirrors.scene.LightGroup;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.LineReflector;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;
import ee.joonasvali.mirrors.scene.label.BoxLabelPhysical;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Joonas Vali April 2017
 */
public class TriangleReflectorGene implements Gene<TriangleReflectorGene> {

  public static final int BOX_SIZE = 3;
  public static final int BOX_SPACE = 2;
  private final double x;
  private final double y;

  private final double x1;
  private final double y1;
  private final double x2;
  private final double y2;
  private final double x3;
  private final double y3;

  private Set<LightGroup> reflectiveGroups;
  private Set<LightGroup> allGroups;

  public TriangleReflectorGene(
      double x, double y, double x1, double y1, double x2, double y2, double x3, double y3) {
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
  public Gene<TriangleReflectorGene> copy() {
    return new TriangleReflectorGene(x, y, x1, y1, x2, y2, x3, y3);
  }

  @Override
  public TriangleReflectorGene getOffspringGene(GeneFactory geneFactory) {
    TriangleReflectorGene gene = new TriangleReflectorGene(
        geneFactory.largeRandom(20) + x,
        geneFactory.largeRandom(20) + y,
        geneFactory.largeRandom(5) + x1,
        geneFactory.largeRandom(5) + y1,
        geneFactory.largeRandom(5) + x2,
        geneFactory.largeRandom(5) + y2,
        geneFactory.largeRandom(5) + x3,
        geneFactory.largeRandom(5) + y3
    );
    if (reflectiveGroups != null) {
      gene.setReflectiveGroups(geneFactory.reflectiveGroups(reflectiveGroups, allGroups));
    }
    if (allGroups != null) {
      gene.setAllGroups(allGroups);
    }
    return gene;
  }

  public void setReflectiveGroups(Set<LightGroup> reflectiveGroups) {
    this.reflectiveGroups = reflectiveGroups;
  }

  public void setAllGroups(Set<LightGroup> allGroups) {
    this.allGroups = allGroups;
  }

  @Override
  public List<Physical> createPhysicals(Environment environment) {
    if (reflectiveGroups != null) {
      List<Physical> physicals = new ArrayList<>();
      int yMin = (int) (Math.min(Math.min(y1, y2), y3) + y);
      int xMax = (int) (Math.max(Math.max(x1, x2), x3) + x);
      int count = 0;
      for (LightGroup group : reflectiveGroups) {
        physicals.add(
            new BoxLabelPhysical(xMax + (BOX_SPACE + 1 + BOX_SIZE) * count, yMin, BOX_SIZE, BOX_SIZE, group.getColor())
        );
        count++;
      }
      return physicals;
    }
    return null;
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Environment environment) {
    List<LinePhysical> lines = new ArrayList<>();
    lines.add(new LineReflector(x1 + x, y1 + y, x2 + x, y2 + y, reflectiveGroups));
    lines.add(new LineReflector(x2 + x, y2 + y, x3 + x, y3 + y, reflectiveGroups));
    lines.add(new LineReflector(x3 + x, y3 + y, x1 + x, y1 + y, reflectiveGroups));
    return lines;
  }
}
