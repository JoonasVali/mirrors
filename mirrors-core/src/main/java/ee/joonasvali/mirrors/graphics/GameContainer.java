package ee.joonasvali.mirrors.graphics;

import ee.joonasvali.mirrors.ModelController;

import java.util.List;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

/**
 * @author Joonas Vali April 2017
 */
public class GameContainer extends JFrame {
  private final ModelController env;
  private final JPanel canvas;
  private final GlobalDispatcher keydispatcher = new GlobalDispatcher();

  public GameContainer(ModelController env) {
    this.env = env;
    this.setLayout(new BorderLayout());
    this.canvas = createCanvas();
    this.canvas.setBorder(BorderFactory.createLineBorder(Color.black));
    this.add(canvas, BorderLayout.CENTER);
    if (env.hasControlPanel()) {
      JPanel panel = env.getControlPanel();
      this.add(panel, BorderLayout.EAST);
    }

    KeyboardFocusManager manager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
    manager.addKeyEventDispatcher(keydispatcher);
  }

  private JPanel createCanvas() {
    JPanel panel = new JPanel() {
      @Override
      public void paintComponent(Graphics g) {
        super.paintComponent(g);
        env.render((Graphics2D) g);
      }
    };
    keydispatcher.addListener(env.getKeyListener());
    return panel;
  }

  private static class GlobalDispatcher implements KeyEventDispatcher {
    private final List<KeyListener> listenerList = new ArrayList<>(1);

    public void addListener(KeyListener listener) {
      this.listenerList.add(listener);
    }

    public void removeListener(KeyListener listener) {
      this.listenerList.remove(listener);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e) {
      if (e.getID() == KeyEvent.KEY_RELEASED) {
        this.listenerList.forEach(k -> k.keyReleased(e));
      }
      return true;
    }
  }
}
