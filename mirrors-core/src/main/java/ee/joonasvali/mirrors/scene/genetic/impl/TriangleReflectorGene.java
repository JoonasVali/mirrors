package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.LineReflector;
import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ParticleGroup;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.label.BoxLabelPhysical;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Joonas Vali April 2017
 */
public class TriangleReflectorGene implements Gene {

  public static final int BOX_SIZE = 3;
  public static final int BOX_SPACE = 2;
  public final double x;
  public final double y;

  public final double x1;
  public final double y1;
  public final double x2;
  public final double y2;
  public final double x3;
  public final double y3;

  private Set<ParticleGroup> reflectiveGroups;

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
  public TriangleReflectorGene copy() {
    return new TriangleReflectorGene(x, y, x1, y1, x2, y2, x3, y3);
  }

  public void setReflectiveGroups(Set<ParticleGroup> reflectiveGroups) {
    this.reflectiveGroups = reflectiveGroups;
  }

  @Override
  public void expressTo(Model model) {
    Set<Integer> groupIds = reflectiveGroups == null ?
        null :
        reflectiveGroups.stream().map(ParticleGroup::getId).collect(Collectors.toSet());

    model.addObject(new LineReflector(x1 + x, y1 + y, x2 + x, y2 + y, groupIds));
    model.addObject(new LineReflector(x2 + x, y2 + y, x3 + x, y3 + y, groupIds));
    model.addObject(new LineReflector(x3 + x, y3 + y, x1 + x, y1 + y, groupIds));

    if (reflectiveGroups != null) {
      int yMin = (int) (Math.min(Math.min(y1, y2), y3) + y);
      int xMax = (int) (Math.max(Math.max(x1, x2), x3) + x);
      int count = 0;
      for (ParticleGroup group : reflectiveGroups) {
        model.addObject(
            new BoxLabelPhysical(xMax + (BOX_SPACE + 1 + BOX_SIZE) * count, yMin, BOX_SIZE, BOX_SIZE, group.getColor())
        );
        count++;
      }
    }
  }

  public Set<ParticleGroup> getReflectiveGroups() {
    return reflectiveGroups;
  }
}
