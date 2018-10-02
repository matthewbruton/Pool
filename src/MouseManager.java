import java.awt.event.*;
import java.awt.*;

public class MouseManager implements MouseListener, MouseMotionListener {
  private int mouseX, mouseY;
  private boolean pressed1, pressed2, pressed3;

  public MouseManager() {

  }

  public void tick() {

  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1)
      pressed1 = true;

    if (e.getButton() == MouseEvent.BUTTON2)
      pressed2 = true;

    if (e.getButton() == MouseEvent.BUTTON3)
      pressed3 = true;
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON1)
      pressed1 = false;

    if (e.getButton() == MouseEvent.BUTTON2)
      pressed2 = false;

    if (e.getButton() == MouseEvent.BUTTON3)
      pressed3 = false;

  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  @Override
  public void mouseMoved(MouseEvent e) {
    mouseX = e.getX();
    mouseY = e.getY();
  }

  @Override
  public void mouseDragged(MouseEvent e) {

  }

  // GETTERS
  public boolean isLeftPressed() {
    return pressed1;
  }

  public boolean isMiddlePressed() {
    return pressed2;
  }

  public boolean isRightPressed() {
    return pressed3;
  }

  public int getMouseX() {
    return mouseX;
  }

  public int getMouseY() {
    return mouseY;
  }

}
