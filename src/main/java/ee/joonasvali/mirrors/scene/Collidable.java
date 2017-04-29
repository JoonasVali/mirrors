package ee.joonasvali.mirrors.scene;

public interface Collidable {
  boolean isCollision(Physical object);
  void actCollision(Physical object, Environment environment);
}
