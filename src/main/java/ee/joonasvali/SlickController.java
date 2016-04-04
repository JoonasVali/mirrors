package ee.joonasvali;

import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Joonas Vali April 2016
 */
public class SlickController extends BasicGame {
  private final static Logger log = LoggerFactory.getLogger(SlickController.class);
  private final EnvironmentController environmentController;

  public SlickController(EnvironmentController environmentController) {
    super("Mirrors");
    this.environmentController = environmentController;
  }

  @Override
  public void init(GameContainer gameContainer) throws SlickException {
    environmentController.init();
  }

  @Override
  public void update(GameContainer gameContainer, int i) throws SlickException {
    environmentController.update(i);
  }

  @Override
  public void render(GameContainer gameContainer, Graphics graphics) throws SlickException {
    environmentController.render(graphics);
  }

  @Override
  public void keyPressed(int key, char c) {
    environmentController.keyPressed(key, c);
  }

  @Override
  public void keyReleased(int key, char c) {
    environmentController.keyReleased(key, c);
  }
}
