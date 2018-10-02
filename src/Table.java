import java.awt.*;
import java.util.ArrayList;

public class Table {
  private double width, height, boarder, pocketRadius, centerX, centerY;
  // private int[] pointsX, pointsY;
  private Game game;
  private Ball cueBall;
  private Color felt = new Color(19, 134, 64);
  private Color cushion = new Color(20, 73, 35);
  private Color rail = new Color(81, 42, 35);
  private int[] pointsULX, pointsURX, pointsRX, pointsLRX, pointsLLX, pointsLX, pointsULY,
      pointsURY, pointsRY, pointsLRY, pointsLLY, pointsLY;
  private ArrayList<Pocket> pockets = new ArrayList<Pocket>();

  // According to WPA/BCA equipment specifications, the weight may be from 5.5 to 6 oz (156–170 g)
  // with a diameter of 2.250 inch (57.15 mm), plus or minus 0.005 inch (0.127 mm)
  // ball-cloth coefficient of rolling friction 0.005 - 0.015
  // 4 -> 10 across 472
  public Table(Game gm, double w, double h, double b, double p) {
    game = gm;
    width = w;
    height = h;
    boarder = b;
    pocketRadius = p;

    centerX = game.width / 2;
    centerY = game.height / 2;

    pockets.add(
        new Pocket(pocketRadius, centerX - width / 2 + boarder, centerY - height / 2 + boarder));
    pockets.add(new Pocket(pocketRadius, centerX, centerY - height / 2 + boarder));
    pockets.add(
        new Pocket(pocketRadius, centerX + width / 2 - boarder, centerY - height / 2 + boarder));
    pockets.add(
        new Pocket(pocketRadius, centerX - width / 2 + boarder, centerY + height / 2 - boarder));
    pockets.add(new Pocket(pocketRadius, centerX, centerY + height / 2 - boarder));
    pockets.add(
        new Pocket(pocketRadius, centerX + width / 2 - boarder, centerY + height / 2 - boarder));

    pointsULX = new int[] {(int) (centerX - width / 2 + boarder + pocketRadius),
        (int) (centerX - pocketRadius), (int) (centerX - pocketRadius * 3 / 2),
        (int) (centerX - width / 2 + boarder + pocketRadius * 3 / 2)};
    pointsULY =
        new int[] {(int) (centerY - height / 2 + boarder), (int) (centerY - height / 2 + boarder),
            (int) (centerY - height / 2 + boarder + pocketRadius / 2),
            (int) (centerY - height / 2 + boarder + pocketRadius / 2)};
    pointsURX = new int[] {(int) (centerX + width / 2 - boarder - pocketRadius),
        (int) (centerX + pocketRadius), (int) (centerX + pocketRadius * 3 / 2),
        (int) (centerX + width / 2 - boarder - pocketRadius * 3 / 2)};
    pointsURY =
        new int[] {(int) (centerY - height / 2 + boarder), (int) (centerY - height / 2 + boarder),
            (int) (centerY - height / 2 + boarder + pocketRadius / 2),
            (int) (centerY - height / 2 + boarder + pocketRadius / 2)};
    pointsRX = new int[] {(int) (centerX + width / 2 - boarder),
        (int) (centerX + width / 2 - boarder - pocketRadius / 2),
        (int) (centerX + width / 2 - boarder - pocketRadius / 2),
        (int) (centerX + width / 2 - boarder)};
    pointsRY = new int[] {(int) (centerY - height / 2 + boarder + pocketRadius),
        (int) (centerY - height / 2 + boarder + pocketRadius * 3 / 2),
        (int) (centerY + height / 2 - boarder - pocketRadius * 3 / 2),
        (int) (centerY + height / 2 - boarder - pocketRadius)};
    pointsLRX = new int[] {(int) (centerX + width / 2 - boarder - pocketRadius),
        (int) (centerX + pocketRadius), (int) (centerX + pocketRadius * 3 / 2),
        (int) (centerX + width / 2 - boarder - pocketRadius * 3 / 2)};
    pointsLRY =
        new int[] {(int) (centerY + height / 2 - boarder), (int) (centerY + height / 2 - boarder),
            (int) (centerY + height / 2 - boarder - pocketRadius / 2),
            (int) (centerY + height / 2 - boarder - pocketRadius / 2)};
    pointsLLX = new int[] {(int) (centerX - width / 2 + boarder + pocketRadius),
        (int) (centerX - pocketRadius), (int) (centerX - pocketRadius * 3 / 2),
        (int) (centerX - width / 2 + boarder + pocketRadius * 3 / 2)};
    pointsLLY =
        new int[] {(int) (centerY + height / 2 - boarder), (int) (centerY + height / 2 - boarder),
            (int) (centerY + height / 2 - boarder - pocketRadius / 2),
            (int) (centerY + height / 2 - boarder - pocketRadius / 2)};
    pointsLX = new int[] {(int) (centerX - width / 2 + boarder),
        (int) (centerX - width / 2 + boarder + pocketRadius / 2),
        (int) (centerX - width / 2 + boarder + pocketRadius / 2),
        (int) (centerX - width / 2 + boarder)};
    pointsLY = new int[] {(int) (centerY - height / 2 + boarder + pocketRadius),
        (int) (centerY - height / 2 + boarder + pocketRadius * 3 / 2),
        (int) (centerY + height / 2 - boarder - pocketRadius * 3 / 2),
        (int) (centerY + height / 2 - boarder - pocketRadius)};

  }

  public void tick() {

  }

  public void render(Graphics g) {
    g.setColor(rail);
    g.fillRect((int) (centerX - width / 2), (int) (centerY - height / 2), (int) width,
        (int) height);

    g.setColor(felt);
    g.fillRect((int) (centerX - width / 2 + boarder), (int) (centerY - height / 2 + boarder),
        (int) (width - 2 * boarder), (int) (height - 2 * boarder));

    for (Pocket p : pockets)
      p.render(g);

    g.setColor(cushion);
    g.fillPolygon(pointsULX, pointsULY, 4);
    g.fillPolygon(pointsURX, pointsURY, 4);
    g.fillPolygon(pointsRX, pointsRY, 4);
    g.fillPolygon(pointsLRX, pointsLRY, 4);
    g.fillPolygon(pointsLLX, pointsLLY, 4);
    g.fillPolygon(pointsLX, pointsLY, 4);

    g.setColor(rail);
    g.fillRect((int) (centerX - width / 2), (int) (centerY - height), (int) (width),
        (int) pocketRadius * 2);
    g.setColor(Color.black);
    g.fillRect((int) (centerX - width / 2 + pocketRadius / 2),
        (int) (centerY - height + pocketRadius / 2), (int) (width - pocketRadius),
        (int) pocketRadius);
  }

  // PHYSICS

  // SETTERS & GETTERS
  public double getWidth() {
    return width;
  }

  public void setWidth(double w) {
    width = w;
  }

  public void setHeight(double h) {
    height = h;
  }

  public void setColorFelt(int r, int g, int b) {
    felt = new Color(r, g, b);
  }

  public void setColorCushion(int r, int g, int b) {
    cushion = new Color(r, g, b);
  }

  public void setColorRail(int r, int g, int b) {
    rail = new Color(r, g, b);
  }

  public ArrayList<Pocket> getPockets() {
    return pockets;
  }

  public double getLeftWall() {
    return centerX - width / 2 + pocketRadius / 2;
  }

  public double getUpperWall() {
    return centerY - height + pocketRadius / 2;
  }

  public double getCenterX() {
    return centerX;
  }

  public double getCenterY() {
    return centerY;
  }

  public double getHeight() {
    return height;
  }

  public double getBoarder() {
    return boarder;
  }

  // private int[] pointsULX, pointsURX, pointsRX, pointsLRX, pointsLLX, pointsLX, pointsULY,
  // pointsURY, pointsRY, pointsLRY, pointsLLY, pointsLY;

  public int[] getPointsULX() {
    return pointsULX;
  }

  public int[] getPointsURX() {
    return pointsURX;
  }

  public int[] getPointsRX() {
    return pointsRX;
  }

  public int[] getPointsLRX() {
    return pointsLRX;
  }

  public int[] getPointsLLX() {
    return pointsLLX;
  }

  public int[] getPointsLX() {
    return pointsLX;
  }

  public int[] getPointsULY() {
    return pointsULY;
  }

  public int[] getPointsURY() {
    return pointsURY;
  }

  public int[] getPointsRY() {
    return pointsRY;
  }

  public int[] getPointsLRY() {
    return pointsLRY;
  }

  public int[] getPointsLLY() {
    return pointsLLY;
  }

  public int[] getPointsLY() {
    return pointsLY;
  }

}
