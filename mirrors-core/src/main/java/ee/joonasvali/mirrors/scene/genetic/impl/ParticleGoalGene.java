package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ParticleGoal;
import ee.joonasvali.mirrors.scene.ParticleGoalAction;
import ee.joonasvali.mirrors.scene.ParticleGroup;
import ee.joonasvali.mirrors.scene.LinePhysical;
import ee.joonasvali.mirrors.scene.Physical;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.GeneFactory;

import java.awt.*;
import java.util.Collections;
import java.util.List;

public class ParticleGoalGene implements Gene {
  private final double radius;
  private final double x;
  private final double y;
  private final Color internalColor;
  private final ParticleGroup targetParticleGroup;

  public ParticleGoalGene(double radius, double x, double y, Color internalColor, ParticleGroup targetParticleGroup) {
    this.targetParticleGroup = targetParticleGroup;
    this.internalColor = internalColor;
    this.radius = radius;
    this.x = x;
    this.y = y;
  }

  @Override
  public ParticleGoalGene copy() {
    return new ParticleGoalGene(radius, x, y, internalColor, targetParticleGroup);
  }

  @Override
  public ParticleGoalGene getOffspringGene(GeneFactory geneFactory) {
    return new ParticleGoalGene(radius, x, y, internalColor, targetParticleGroup);
  }

  @Override
  public List<Physical> createPhysicals(Model model) {
    ParticleGoalAction action = particle -> {
      model.remove(particle);

      if (targetParticleGroup.getId() == particle.getParticleGroup().getId()) {
        model.addScore((particle).getIntensity());
      }
    };
    return Collections.singletonList(
        new ParticleGoal(x - radius / 2, y - radius / 2, 0, radius, action, internalColor)
    );
  }

  @Override
  public List<LinePhysical> createLinePhysicals(Model model) {
    return null;
  }
}
