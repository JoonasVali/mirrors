package ee.joonasvali.scene;

public interface Collidable {
  boolean isCollision(Physical object);
  void actCollision(Physical object, Environment environment);
}
