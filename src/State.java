import java.awt.Graphics;

public abstract class State {

  // Class

  protected Game game;

  public State(Game game) {
    this.game = game;
  }

  public abstract void tick();

  public abstract void render(Graphics g);

  // State Manager

  private static State currentState = null;

  public static void setState(State state) {
    currentState = state;
  }

  public static State getState() {
    return currentState;
  }

}
