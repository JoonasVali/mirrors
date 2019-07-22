package ee.joonasvali.mirrors.scene.genetic.impl;

import ee.joonasvali.mirrors.scene.Model;
import ee.joonasvali.mirrors.scene.ParticleGoal;
import ee.joonasvali.mirrors.scene.ParticleGoalAction;
import ee.joonasvali.mirrors.scene.ParticleGroup;
import ee.joonasvali.mirrors.scene.genetic.Gene;

import java.awt.*;

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
  public void expressTo(Model model) {
    ParticleGoalAction action = particle -> {
      model.removeParticle(particle);

      if (targetParticleGroup.getId() == particle.getParticleGroup().getId()) {
        model.addScore((particle).getIntensity());
      }
    };
    model.addObject(
        new ParticleGoal(x - radius / 2, y - radius / 2, 0, radius, action, internalColor)
    );
  }
}
