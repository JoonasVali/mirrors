package ee.joonasvali.scene;

/**
 * Created with IntelliJ IDEA.
 * User: Joss
 * Date: 3/23/14
 * Time: 3:15 PM
 * To change this template use File | Settings | File Templates.
 */
public interface Collidable {
  boolean isCollision(Physical object);
  void actCollision(Physical object, Environment environment);
}
