import java.awt.*;

public class Ball {
  private double theta, phi, radius, mass, positionX, positionY, lastPositionX, lastPositionY,
      velocityX, velocityY, fallTime;
  private int value;
  private double density = 0.00005239668; // kg / Volume .16 kg ball
  private Color ballColor;
  private boolean collided = false;
  private boolean falling = false;
  private boolean movable = false;
  private Game game;
  private Table table;
  private Color offWhite = new Color(245, 239, 213);

  // According to WPA/BCA equipment specifications, the weight may be from 5.5 to 6 oz (156–170 g)
  // with a diameter of 2.250 inch (57.15 mm), plus or minus 0.005 inch (0.127 mm)
  // when if ( cueBall.getVelocity < .001) > cueBall.setVelocity(0)
  public Ball(Game gm, Table t, double r, double x, double y, int v) {
    radius = r;
    pickMass();
    fallTime = 0;
    value = v;
    pickColor();
    game = gm;
    table = t;
    resetPosition();
    if (value == 0)
      movable = true;
  }

  public void pickMass() {
    mass = density * 4 / 3 * Math.PI * radius * radius * radius;
  }

  public void pickColor() {
    // change to images so it can support stripes OR possibly do drawOval around fillOval for a ring
    // which could represent stripes YES DO THIS
    if (value == 0)
      ballColor = offWhite;
    else if (value % 8 == 1)
      ballColor = new Color(241, 165, 20);
    else if (value % 8 == 2)
      ballColor = new Color(25, 69, 194);
    else if (value % 8 == 3)
      ballColor = new Color(215, 34, 23);
    else if (value % 8 == 4)
      ballColor = new Color(41, 32, 81);
    else if (value % 8 == 5)
      ballColor = new Color(230, 118, 40);
    else if (value % 8 == 6)
      ballColor = new Color(14, 110, 65);
    else if (value % 8 == 7)
      ballColor = new Color(113, 21, 23);
    else if (value == 8)
      ballColor = new Color(12, 10, 11);
    else
      ballColor = Color.gray;
  }

  // TICK & RENDER
  public void tick() {
    if (value == 0) { // add constrainst so only before game || after scratch && in kitchen
      if (movable) {
        if (game.getKeyManager().w)
          positionY -= 30.0 / game.getFPS();
        if (game.getKeyManager().s)
          positionY += 30.0 / game.getFPS();
        if (game.getKeyManager().a)
          positionX -= 30.0 / game.getFPS();
        if (game.getKeyManager().d)
          positionX += 30.0 / game.getFPS();

        if (!inFelt())
          resetPosition();
        if (positionX > table.getCenterX() - table.getWidth() / 4 + table.getBoarder() / 2)
          positionX = table.getCenterX() - table.getWidth() / 4 + table.getBoarder() / 2;
      }
    }
    velocityX *= 0.99; // make these more accurate and based off calculations not just *=
    velocityY *= 0.99;
    lastPositionX = positionX;
    lastPositionY = positionY;
    positionX += velocityX;
    positionY += velocityY;

    if (inContactCushionUp()) {
      velocityX *= 0.85;
      velocityY *= -0.85;
      positionY = table.getPointsULY()[2] + radius;
    }

    if (inContactCushionDown()) {
      velocityX *= 0.85;
      velocityY *= -0.85;
      positionY = table.getPointsLLY()[2] - radius;
    }

    if (inContactCushionLeft()) {
      velocityX *= -0.85;
      velocityY *= 0.85;
      positionX = table.getPointsLX()[1] + radius;
    }
    if (inContactCushionRight()) {
      velocityX *= -0.85;
      velocityY *= 0.85;
      positionX = table.getPointsRX()[1] - radius;
    }
  }

  public void render(Graphics g) {

    g.setColor(ballColor);
    g.fillOval((int) (positionX - radius), (int) (positionY - radius), (int) radius * 2,
        (int) radius * 2);
    if (value > 8) {
      g.setColor(offWhite);
      g.fillOval((int) (positionX - radius * 2 / 3), (int) (positionY - radius * 2 / 3),
          (int) radius * 4 / 3, (int) radius * 4 / 3);
    }

    // if stripES check value for stripE color
  }

  public void resetPosition() {
    if (value == 0) {
      positionX = table.getCenterX() - table.getWidth() / 4 + table.getBoarder() / 2;
      positionY = table.getCenterY();
    } else if (value == 1) {
      positionX = table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2;
      positionY = table.getCenterY();
    } else if (value == 2) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 2;
      positionY = table.getCenterY() + (radius + .5);
    } else if (value == 3) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 4;
      positionY = table.getCenterY() - (2 * radius + .5);
    } else if (value == 4) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 6;
      positionY = table.getCenterY() + (3 * radius + .5);
    } else if (value == 5) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 6;
      positionY = table.getCenterY() - (radius + .5);
    } else if (value == 6) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 8;
      positionY = table.getCenterY() + (2 * radius + .5);
    } else if (value == 7) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 8;
      positionY = table.getCenterY() - (4 * radius + .5);
    } else if (value == 8) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 4;
      positionY = table.getCenterY();
    } else if (value == 9) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 2;
      positionY = table.getCenterY() - (radius + .5);
    } else if (value == 10) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 4;
      positionY = table.getCenterY() + (2 * radius + .5);
    } else if (value == 11) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 6;
      positionY = table.getCenterY() - (3 * radius + .5);
    } else if (value == 12) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 6;
      positionY = table.getCenterY() + (radius + .5);
    } else if (value == 13) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 8;
      positionY = table.getCenterY();
    } else if (value == 14) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 8;
      positionY = table.getCenterY() + (4 * radius + .5);
    } else if (value == 15) {
      positionX =
          table.getCenterX() + table.getWidth() / 4 - table.getBoarder() / 2 + (radius + .5) * 8;
      positionY = table.getCenterY() - (2 * radius + .5);
    } else {
      positionX = table.getCenterX();
      positionY = table.getCenterY();
    }
  }

  // PHYSICS
  public boolean inContact(Ball b2) {
    // (x2-x1)^2 + (y1-y2)^2 <= (r1+r2)^2
    if (Math.pow(b2.getPositionX() - this.getPositionX(), 2)
        + Math.pow(b2.getPositionY() - this.getPositionY(), 2) <= Math
            .pow(b2.getRadius() + this.getRadius(), 2))
      return true;
    else
      return false;
  }

  public void calcPhi(Ball ball2) {
    phi = Math.atan((ball2.getPositionY() - this.getPositionY())
        / (ball2.getPositionX() - this.getPositionX()));
  }

  public double getVelocityX2(Ball ball2) {
    calcPhi(ball2);
    return ((getVelocity() * Math.cos(getTheta() - phi) * (mass - ball2.getMass())
        + 2 * ball2.getMass() * ball2.getVelocity() * Math.cos(ball2.getTheta() - phi))
        * Math.cos(phi) / (mass + ball2.getMass()))
        + (getVelocity() * Math.sin(getTheta() - phi) * Math.cos(phi + Math.PI / 2));
  }

  public double getVelocityY2(Ball ball2) {
    calcPhi(ball2);
    return ((getVelocity() * Math.cos(getTheta() - phi) * (mass - ball2.getMass())
        + 2 * ball2.getMass() * ball2.getVelocity() * Math.cos(ball2.getTheta() - phi))
        * Math.sin(phi) / (mass + ball2.getMass()))
        + (getVelocity() * Math.sin(getTheta() - phi) * Math.sin(phi + Math.PI / 2));
  }

  public boolean overPocket(Pocket p) {
    if (Math.pow(p.getPositionX() - this.getPositionX(), 2)
        + Math.pow(p.getPositionY() - this.getPositionY(), 2) <= Math.pow(this.getRadius(), 2))
      return true;
    else
      return false;
  }

  public boolean inContact(Pocket p) {
    // (x2-x1)^2 + (y1-y2)^2 <= (r1+r2)^2
    if (Math.pow(p.getPositionX() - this.getPositionX(), 2)
        + Math.pow(p.getPositionY() - this.getPositionY(), 2) <= Math
            .pow(p.getRadius() + this.getRadius(), 2))
      return true;
    else
      return false;
  }

  public void calcPhi(Pocket p) {
    phi = Math
        .atan((p.getPositionY() - this.getPositionY()) / (p.getPositionX() - this.getPositionX()));
  }

  public double getVelocityX2(Pocket p) {
    calcPhi(p);
    return ((getVelocity() * Math.cos(getTheta() - phi) * (mass - 4 * mass)) * Math.cos(phi)
        / (mass + 4 * mass))
        + (getVelocity() * Math.sin(getTheta() - phi) * Math.cos(phi + Math.PI / 2));
  }

  public double getVelocityY2(Pocket p) {
    calcPhi(p);
    return ((getVelocity() * Math.cos(getTheta() - phi) * (mass - 4 * mass)) * Math.sin(phi)
        / (mass + 4 * mass))
        + (getVelocity() * Math.sin(getTheta() - phi) * Math.sin(phi + Math.PI / 2));
  }

  public boolean inRail() {
    if (positionX > table.getCenterX() - table.getWidth() / 2
        && positionX < table.getCenterX() + table.getWidth() / 2
        && positionY > table.getCenterY() - table.getHeight() / 2
        && positionY < table.getCenterY() + table.getHeight() / 2)
      return true;
    return false;
  }

  public boolean inFelt() {
    if (positionX > table.getCenterX() - table.getWidth() / 2 + table.getBoarder()
        && positionX < table.getCenterX() + table.getWidth() / 2 - table.getBoarder()
        && positionY > table.getCenterY() - table.getHeight() / 2 + table.getBoarder()
        && positionY < table.getCenterY() + table.getHeight() / 2 - table.getBoarder())
      return true;
    return false;
  }

  // SETTERS & GETTERS
  public double getMass() {
    return mass;
  }

  public void setMass(double m) {
    mass = m;
  }

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

  public double getVelocity() {
    return Math.sqrt(Math.pow(velocityX, 2) + Math.pow(velocityY, 2));
  }

  public double getVelocityX() {
    return velocityX;
  }

  public double getVelocityY() {
    return velocityY;
  }

  public void setVelocityX(double vx) {
    velocityX = vx;
  }

  public void setVelocityY(double vy) {
    velocityY = vy;
  }

  public int getValue() {
    return value;
  }

  public double getTheta() {
    if (velocityX == 0) {
      if (velocityY >= 0)
        theta = Math.PI / 2;
      else
        theta = -Math.PI / 2;
    } else {
      theta = Math.atan(velocityY / velocityX);

      if (velocityX < 0)
        theta += Math.PI;
    }
    return theta;
  }

  public boolean getCollided() {
    return collided;
  }

  public void setCollided(boolean b) {
    collided = b;
  }

  public double getFallTime() {
    return fallTime;
  }

  public void addFallTime() {
    fallTime += 1.0 / game.getFPS();
  }

  public void setFallTime(double f) {
    fallTime = f;
  }

  public boolean getFalling() {
    return falling;
  }

  public void setFalling(boolean b) {
    falling = b;
  }

  public boolean inContactCushionLeft() { // need setCollided buffer
    if (positionX - radius <= table.getPointsLX()[1] && positionY >= table.getPointsLY()[1]
        && positionY <= table.getPointsLY()[2])
      return true;
    return false;
  }

  public boolean inContactCushionRight() { // need setCollided buffer
    if (positionX + radius >= table.getPointsRX()[1] && positionY >= table.getPointsRY()[1]
        && positionY <= table.getPointsRY()[2])
      return true;
    return false;
  }

  public boolean inContactCushionUp() { // need setCollided buffer
    if (positionY - radius <= table.getPointsULY()[2]
        && ((positionX >= table.getPointsULX()[3] && positionX <= table.getPointsULX()[2])
            || (positionX >= table.getPointsURX()[2] && positionX <= table.getPointsURX()[3])))
      return true;
    return false;
  }

  public boolean inContactCushionDown() { // need setCollided buffer
    if (positionY + radius >= table.getPointsLLY()[2]
        && ((positionX >= table.getPointsLLX()[3] && positionX <= table.getPointsLLX()[2])
            || (positionX >= table.getPointsLRX()[2] && positionX <= table.getPointsLRX()[3])))
      return true;
    return false;
  }

  public void setMovable(boolean b) {
    movable = b;
  }

  public double getLastPositionX() {
    return lastPositionX;
  }

  public double getLastPositionY() {
    return lastPositionY;
  }

}
