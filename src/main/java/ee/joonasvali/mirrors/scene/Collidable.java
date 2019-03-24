package ee.joonasvali.mirrors.scene;

public interface Collidable {
  boolean isCollision(Light object);
  void actCollision(Light object, Environment environment);
}
