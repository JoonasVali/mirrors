package ee.joonasvali.mirrors.scene;

public interface Collidable {
  boolean isCollision(Particle object);

  void actCollision(Particle object, Model model);
}
