import java.awt.event.*;

public class KeyManager implements KeyListener {

  private boolean[] keys;
  public boolean w, a, s, d;

  public KeyManager() {
    keys = new boolean[256];
  }

  public void tick() {
    w = keys[KeyEvent.VK_W];
    s = keys[KeyEvent.VK_S];
    a = keys[KeyEvent.VK_A];
    d = keys[KeyEvent.VK_D];
  }

  @Override
  public void keyPressed(KeyEvent e) {
    keys[e.getKeyCode()] = true;
    System.out.println("Pressed");
  }

  @Override
  public void keyReleased(KeyEvent e) {
    keys[e.getKeyCode()] = false;
    System.out.println("Released");
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }
}
