package ee.joonasvali.scene;

/**
 * Created with IntelliJ IDEA.
 * User: Joss
 * Date: 3/23/14
 * Time: 3:39 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class BasePhysical implements Physical{
  protected double x, y;
  protected double width, height;
  protected double rotation;

  public BasePhysical(double x, double y, double rotation, double width, double height){
    this.x = x;
    this.y = y;
    this.rotation = rotation;
    this.width = width;
    this.height = height;
  }

  @Override
  public double getX() {
    return x;
  }

  @Override
  public double getY() {
    return y;
  }

  @Override
  public double getRotation() {
    return rotation;
  }

  @Override
  public double getWidth() {
    return width;
  }

  @Override
  public double getHeight() {
    return height;
  }

}
