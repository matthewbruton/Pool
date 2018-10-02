import java.awt.*;

public class Pocket {
  private double radius, positionX, positionY;
  private int value;
  private double density = 0.00005239668; // kg / Volume .16 kg ball
  private Color ballColor;
  private boolean collided = false;
  private Game game;

  // According to WPA/BCA equipment specifications, the weight may be from 5.5 to 6 oz (156–170 g)
  // with a diameter of 2.250 inch (57.15 mm), plus or minus 0.005 inch (0.127 mm)
  // when if ( cueBall.getVelocity < .001) > cueBall.setVelocity(0)
  public Pocket(double r, double x, double y) {
    radius = r;
    positionX = x;
    positionY = y;
  }

  // TICK & RENDER
  public void tick() {

  }

  public void render(Graphics g) {
    g.setColor(Color.black);
    g.fillOval((int) (positionX - radius), (int) (positionY - radius), (int) radius * 2,
        (int) radius * 2);
  }

  // PHYSICS

  // SETTERS & GETTERS
  public double getRadius() {
    return radius;
  }

  public void setRadius(double r) {
    radius = r;
  }

  public double getPositionX() {
    return positionX;
  }

  public double getPositionY() {
    return positionY;
  }

  public void setPositionX(double px) {
    positionX = px;
  }

  public void setPositionY(double py) {
    positionY = py;
  }

}
