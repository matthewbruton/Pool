import java.awt.*;

public class Cue {
  private double theta, alpha, beta, drawPositionX, drawPositionY, length, mass, positionX,
      positionY, velocityX, velocityY, acceleration, cueTipRadius, cueButtRadius;
  private int[] pointsX, pointsY;
  private double drawDistance = 0, maxDrawDistance;
  private boolean drawUp = true, shooting = false, shot = false, impacted = false;
  private Game game;
  private Ball cueBall;

  // According to WPA/BCA equipment specifications, the weight may be from 5.5 to 6 oz (156–170 g)
  // with a diameter of 2.250 inch (57.15 mm), plus or minus 0.005 inch (0.127 mm)
  // ball-cloth coefficient of rolling friction 0.005 - 0.015
  // 4 -> 10 across 472
  public Cue(Game gm, double l, Ball c) {
    game = gm;
    length = l; // 472
    cueBall = c;

    mass = 0.6; // .6 kg
    cueTipRadius = 2;
    cueButtRadius = 5;
    positionX = 0;
    positionY = 0;
    velocityX = 0;
    velocityY = 0;
    acceleration = 0.5;
    pointsX = new int[4];
    pointsY = new int[4];
  }

  public void tick() {
    if (game.getMouseManager().isLeftPressed() && shot == false) {
      if (drawDistance >= 72)
        drawUp = false;
      if (drawDistance <= 0)
        drawUp = true;
      if (drawUp)
        drawDistance++;
      else
        drawDistance--;
      shooting = true;
    }

    if (game.getMouseManager().isLeftPressed() == false && shooting) {
      shooting = false;
      shot = true;
      maxDrawDistance = drawDistance;
    }

    if (drawDistance > -maxDrawDistance && shot)
      drawDistance -= maxDrawDistance * 10 / game.getFPS(); // ***

    if (drawDistance <= 0 && shot && impacted == false)
      impacted = true;

    if (drawDistance <= -maxDrawDistance && shot) {
      shot = false;
      impacted = false;
    }

  }

  public void render(Graphics g) {
    g.setColor(new Color(226, 205, 174));
    g.fillPolygon(pointsX, pointsY, 4);
  }

  // PHYSICS

  public void setPoints() {
    theta = Math.atan((cueBall.getPositionY() - game.getMouseManager().getMouseY())
        / (cueBall.getPositionX() - game.getMouseManager().getMouseX())); // angle between moues and
                                                                          // cue ball
    if (game.getMouseManager().getMouseX() > cueBall.getPositionX())
      theta -= Math.PI;
    alpha = Math.atan(cueTipRadius / cueBall.getRadius()); // angle between cue ball and tip
    beta = Math.atan(cueButtRadius / (cueBall.getRadius() + length)); // angle between cue ball and
                                                                      // butt
    drawPositionX = cueBall.getPositionX() + (drawDistance * Math.cos(theta));
    drawPositionY = cueBall.getPositionY() + (drawDistance * Math.sin(theta));

    pointsX[0] = (int) (drawPositionX
        + Math.sqrt(Math.pow(cueBall.getRadius(), 2) + Math.pow(cueTipRadius, 2))
            * Math.cos(theta - alpha));
    pointsY[0] = (int) (drawPositionY
        + Math.sqrt(Math.pow(cueBall.getRadius(), 2) + Math.pow(cueTipRadius, 2))
            * Math.sin(theta - alpha));
    pointsX[1] = (int) (drawPositionX
        + Math.sqrt(Math.pow(cueBall.getRadius(), 2) + Math.pow(cueTipRadius, 2))
            * Math.cos(theta + alpha));
    pointsY[1] = (int) (drawPositionY
        + Math.sqrt(Math.pow(cueBall.getRadius(), 2) + Math.pow(cueTipRadius, 2))
            * Math.sin(theta + alpha));
    pointsX[2] = (int) (drawPositionX
        + Math.sqrt(Math.pow(cueBall.getRadius() + length, 2) + Math.pow(cueButtRadius, 2))
            * Math.cos(theta + beta));
    pointsY[2] = (int) (drawPositionY
        + Math.sqrt(Math.pow(cueBall.getRadius() + length, 2) + Math.pow(cueButtRadius, 2))
            * Math.sin(theta + beta));
    pointsX[3] = (int) (drawPositionX
        + Math.sqrt(Math.pow(cueBall.getRadius() + length, 2) + Math.pow(cueButtRadius, 2))
            * Math.cos(theta - beta));
    pointsY[3] = (int) (drawPositionY
        + Math.sqrt(Math.pow(cueBall.getRadius() + length, 2) + Math.pow(cueButtRadius, 2))
            * Math.sin(theta - beta));
  }

  public double impactX() {
    return -Math.cos(theta)
        * Math.sqrt(2 * mass * acceleration * maxDrawDistance / cueBall.getMass());
  }

  public double impactY() {
    return -Math.sin(theta)
        * Math.sqrt(2 * mass * acceleration * maxDrawDistance / cueBall.getMass());
  }

  // SETTERS & GETTERS
  public double getLength() {
    return length;
  }

  public void setLength(double l) {
    length = l;
  }

  public double getPositionX() {
    return positionX;
  }

  public void setPositionX(double px) {
    positionX = px;
  }

  public double getPositionY() {
    return positionY;
  }

  public void setPositionY(double py) {
    positionY = py;
  }

  public double getVelocityX() {
    return velocityX;
  }

  public void setVelocityX(double vx) {
    velocityX = vx;
  }

  public double getVelocityY() {
    return velocityY;
  }

  public void setVelocityY(double vy) {
    velocityY = vy;
  }

  public boolean impacted() {
    return impacted;
  }

  public void setDrawDistance(int d) {
    drawDistance = d;
  }

  public boolean getShot() {
    return shot;
  }

  public boolean getShooting() {
    return shooting;
  }
}
