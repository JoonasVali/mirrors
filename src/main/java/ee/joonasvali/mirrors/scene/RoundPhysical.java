package ee.joonasvali.mirrors.scene;

public abstract class RoundPhysical extends BasePhysical{
  double radius;
  public RoundPhysical(double x, double y, double rotation, double radius) {
    super(x, y, rotation, radius * 2 , radius * 2);
    this.radius = radius;
  }

  public double getRadius(){
    return radius;
  }

  public double getCenterX(){
    return x + getRadius();
  }

  public double getCenterY(){
    return y + getRadius();
  }
}
