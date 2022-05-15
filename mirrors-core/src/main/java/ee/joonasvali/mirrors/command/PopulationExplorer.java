package ee.joonasvali.mirrors.command;

import ee.joonasvali.mirrors.DemoModelController;
import ee.joonasvali.mirrors.scene.ModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.GeneticModelBuilder;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import ee.joonasvali.mirrors.scene.genetic.util.SerializationUtil;
import ee.joonasvali.mirrors.util.GenomeChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class PopulationExplorer {
  private static final Logger log = LoggerFactory.getLogger(PopulationExplorer.class);

  public static DemoModelController explore(String[] args) {
    if (args.length < 2) {
      log.error("expected 2nd parameter to be the file pointing to population");
      System.exit(-1);
    }

    if (GraphicsEnvironment.isHeadless()) {
      log.error("This system is not supported as it appears to be headless. GraphicsEnvironment.isHeadless() == true");
      System.exit(-1);
    }

    Path populationFile = Paths.get(args[1]);
    try {
      ArrayList<Genome> collection = new ArrayList<>(SerializationUtil.deserializePopulation(populationFile));
      if(collection.isEmpty()) {
        log.error("No population in provided file.");
        System.exit(-1);
      }

      GenomeChooser genomeChooser = new GenomeChooser(collection);
      GeneticModelBuilder builder = new GeneticModelBuilder(genomeChooser);
      genomeChooser.addSelectionChangedListener((c) -> builder.setReload());
      return new DemoModelController(builder) {

        @Override
        public boolean hasControlPanel() {
          return true;
        }

        @Override
        public JPanel getControlPanel() {
          return genomeChooser;
        }
      };

    } catch (IOException e) {
      log.error("Unable to read file " + populationFile.toAbsolutePath() + " : " + e.getMessage());
      e.printStackTrace();
    }
    return null;

  }
}
