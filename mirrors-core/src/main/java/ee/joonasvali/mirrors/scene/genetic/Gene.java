package ee.joonasvali.mirrors.scene.genetic;

import ee.joonasvali.mirrors.scene.Model;


public interface Gene {
  Gene copy();

  void expressTo(Model model);
}
