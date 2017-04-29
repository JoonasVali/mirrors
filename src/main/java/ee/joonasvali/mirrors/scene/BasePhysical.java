package ee.joonasvali.mirrors.scene;


public abstract class BasePhysical implements Physical{
  protected volatile double x, y;
  protected final double width, height;
  protected final double rotation;

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
