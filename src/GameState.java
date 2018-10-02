import java.awt.Graphics;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;

public class GameState extends State {

  // private Ball ball1;
  private Table table;
  private Cue cue;
  private ArrayList<Ball> balls = new ArrayList<Ball>();
  private ArrayList<Ball> pocketedBalls = new ArrayList<Ball>();
  private double bVX, bVY, b2VX, b2VY;
  private int ballRadius = 9;
  private boolean isMotion;
  private int collisions = 0;

  public GameState(Game gm) {
    super(gm);
    table = new Table(gm, 768, 384, 32, 2 * ballRadius);
    // 1 inch = 8 pixels
    // radius: 9 diameter: 18
    for (int i = 0; i <= 15; i++)
      balls.add(new Ball(gm, table, ballRadius, 10, 10, i));
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 0) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 1) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 2) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 3) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 4) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 5) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 6) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 7) );

    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 8) );

    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 9) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 10) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 11) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 12) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 13) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 14) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 15) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 2) );
    // balls.add( new Ball(gm, table, ballRadius, 10, 10, 9) );

    cue = new Cue(gm, 472, balls.get(0));
  }

  @Override
  public void tick() {
    table.tick();
    cue.tick();

    for (Ball b : balls) {
      b.tick();
    }

    // combine by putting cueBall in ArrayList, issues because not "Ball"
    // need to consider angle and possibly point of collision (done)
    for (Ball b : balls) {
      for (Ball b2 : balls) {
        if (b != b2) {
          if (b.inContact(b2)) {
            // if (b.getCollided() || b2.getCollided()) {
            // b.setCollided(false);
            // b2.setCollided(false);
            // }
            // else {
            //
            // b.setCollided(true);
            // b2.setCollided(true);

            bVX = b.getVelocityX2(b2);
            bVY = b.getVelocityY2(b2);
            b2VX = b2.getVelocityX2(b);
            b2VY = b2.getVelocityY2(b);
            b.setPositionX(b.getLastPositionX());
            b.setPositionY(b.getLastPositionY());
            // *= 0.99 for energy lost to heat
            b.setVelocityX(bVX);
            b.setVelocityY(bVY);
            b2.setVelocityX(b2VX);
            b2.setVelocityY(b2VY);

            collisions++;
            // }
          }
        }
      }
    }

    for (Ball b : balls) {
      b.setFalling(false);
      for (Pocket p : table.getPockets()) {
        if (b.overPocket(p) || b.inContact(p)) {
          b.setFalling(true);
          b.addFallTime();
        }
      }
      if (b.getFalling() == false) {
        b.setFallTime(0);
      } else if (b.getFallTime() >= Math.sqrt(b.getRadius() * 0.00635 / 2 / 4.095)) {
        b.setVelocityX(0);
        b.setVelocityY(0);
        b.setPositionX(table.getLeftWall() + b.getRadius());
        b.setPositionY(table.getUpperWall() + ballRadius * 2 - b.getRadius());
        pocketedBalls.add(b);
      }
    }
    balls.removeAll(pocketedBalls);

    for (Ball b : balls) {
      for (Pocket p : table.getPockets()) {
        if (b.inContact(p) && b.inRail() && b.inFelt() == false) {
          if (b.getCollided()) {
            b.setCollided(false);
          } else {
            b.setCollided(true);
            // *= 0.99 for energy lost to heat
            b.setVelocityX(b.getVelocityX2(p));
            b.setVelocityY(b.getVelocityY2(p));
          }
        }
      }
    }

    for (Ball b : pocketedBalls) {
      b.setPositionX(
          table.getLeftWall() + b.getRadius() + pocketedBalls.indexOf(b) * ballRadius * 2);
    }

    if (cue.impacted()) {
      balls.get(0).setVelocityX(cue.impactX());
      balls.get(0).setVelocityY(cue.impactY());
    }

    isMotion = false;
    for (Ball b : balls) {
      if (b.getVelocity() > 0.01) {
        isMotion = true;
        break;
      }
    }

    // if ( isMotion == false && collisions == 0 )
    // balls.get(0).setMovable(true);
    if (isMotion == false || cue.getShot()) {
      if (containsCueBall(pocketedBalls, 0) >= 0) {
        balls.add(0, pocketedBalls.remove(containsCueBall(pocketedBalls, 0)));
        balls.get(0).setMovable(true);
      }

      cue.setPoints();

    } else {
      cue.setDrawDistance(0);
    }

    if (cue.getShooting()) {
      collisions = 0;
      balls.get(0).setMovable(false);
    }
  }

  @Override
  public void render(Graphics g) {
    g.drawImage(game.getImageWood(), 0, 0, null);
    // g.setColor(new Color(210, 181, 133));
    // g.fillRect(0, 0, game.width, game.height);
    table.render(g);

    for (Ball b : balls) {
      b.render(g);
    }

    for (Ball b : pocketedBalls) {
      b.render(g);
    }

    cue.render(g);
  }

  private int containsCueBall(ArrayList<Ball> list, int v) {
    for (Ball b : list) {
      if (b.getValue() == v)
        return list.indexOf(b);
    }
    return -1;
  }
}
