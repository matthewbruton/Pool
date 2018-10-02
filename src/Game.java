import java.awt.image.BufferStrategy;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Game implements Runnable {

  private Display display;
  public int width, height;
  public String title;

  private boolean running = false;
  private Thread thread;

  private BufferStrategy bs;
  private Graphics g;

  private int fps;

  private BufferedImage wood;

  // States
  private State gameState;

  // Input
  private KeyManager keyManager;
  private MouseManager mouseManager;

  public Game(String title, int width, int height) {
    this.width = width;
    this.height = height;
    this.title = title;
    keyManager = new KeyManager();
    mouseManager = new MouseManager();

  }

  private void init() {
    display = new Display(title, width, height);
    display.getFrame().addKeyListener(keyManager);
    display.getFrame().addMouseListener(mouseManager);
    display.getFrame().addMouseMotionListener(mouseManager);
    display.getCanvas().addMouseListener(mouseManager);
    display.getCanvas().addMouseMotionListener(mouseManager);

    wood = ImageLoader.loadImage("wood.jpg");

    gameState = new GameState(this);
    State.setState(gameState);
  }

  private void tick() {
    keyManager.tick();
    mouseManager.tick();

    if (State.getState() != null)
      State.getState().tick();
  }

  private void render() {
    bs = display.getCanvas().getBufferStrategy();
    if (bs == null) {
      display.getCanvas().createBufferStrategy(3);
      return;
    }

    g = bs.getDrawGraphics();
    g.clearRect(0, 0, width, height);
    // draw here
    if (State.getState() != null)
      State.getState().render(g);

    bs.show();
    g.dispose();
  }

  public void run() {

    init();

    fps = 60;
    double timePerTick = 1000000000 / fps;
    double delta = 0;
    long now;
    long lastTime = System.nanoTime();
    long timer = 0;
    int ticks = 0;

    while (running) {
      now = System.nanoTime();
      delta += (now - lastTime) / timePerTick;
      timer += now - lastTime;
      lastTime = now;

      if (delta >= 1) {
        tick();
        render();
        ticks++;
        delta--;
      }

      if (timer >= 1000000000) {
        ticks = 0;
        timer = 0;
      }
    }

    stop();

  }

  public KeyManager getKeyManager() {
    return keyManager;
  }

  public MouseManager getMouseManager() {
    return mouseManager;
  }

  public Image getImageWood() {
    return wood;
  }

  public int getFPS() {
    return fps;
  }

  public synchronized void start() {
    if (running)
      return;
    running = true;
    thread = new Thread(this);
    thread.start();
  }

  public synchronized void stop() {
    if (!running)
      return;
    running = false;
    try {
      thread.join();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

}

