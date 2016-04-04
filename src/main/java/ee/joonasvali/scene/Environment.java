package ee.joonasvali.scene;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Environment {
  private final LinkedList<Physical> objects = new LinkedList<>();
  private double score = 0;
  private int lightCount = 0;

  public double getScore() {
    return score;
  }

  public void addScore(double score) {
    this.score += score;
  }

  public void addObject(Physical physical) {
    if(physical instanceof Light) lightCount++;
    this.objects.add(physical);
  }

  public void actUntilNoLightLeft() {
    do {
      act(1);
    } while (isLightRemaining());
  }

  public void act(int delta) {
    // check collisions for light and objects

    List<Physical> objects = new ArrayList<>(getObjects());

    for(Physical object : objects) {
      if(object instanceof Collidable) {
        for(Physical object2 : objects){
          if(object2 instanceof Light) {
            if(((Collidable) object).isCollision(object2)) {
              ((Collidable) object).actCollision(object2, this);
            }
          }
        }
      }
    }

    for(Physical object : objects) {
      if(object instanceof Activatable) {
        ((Activatable) object).activate();
      }
    }
  }

  public List<Physical> getObjects() {
    return objects;
  }

  public void remove(Physical object) {
    if(object instanceof Light){
      lightCount--;
    }
    objects.remove(object);
  }

  public boolean isLightRemaining() {
    return lightCount > 0;
  }
}
