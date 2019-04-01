package ee.joonasvali.mirrors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class EvolutionPropertyLoader {
  private final static Logger log = LoggerFactory.getLogger(EvolutionPropertyLoader.class);

  public EvolutionProperties loadProperties(Path propertiesFile) {
    EvolutionProperties properties;
    try {
      log.info("Properties read from: " + propertiesFile);
      Properties props = new Properties();
      try (InputStream stream = Files.newInputStream(propertiesFile)) {
        props.load(stream);
      }

      properties = new EvolutionProperties(
          Integer.parseInt(props.getProperty("elites", String.valueOf(DefaultEvolutionPropertyValues.ELITES))),
          Integer.parseInt(props.getProperty("concurrent", String.valueOf(DefaultEvolutionPropertyValues.CONCURRENT))),
          Integer.parseInt(props.getProperty("target.fitness", String.valueOf(DefaultEvolutionPropertyValues.TARGET_FITNESS))),
          Boolean.parseBoolean(props.getProperty("keep.machine.alive", String.valueOf(DefaultEvolutionPropertyValues.KEEP_MACHINE_ALIVE))),
          Double.parseDouble(props.getProperty("gene.mutation.rate", String.valueOf(DefaultEvolutionPropertyValues.GENE_MUTATION_RATE))),
          Double.parseDouble(props.getProperty("gene.deletion.rate", String.valueOf(DefaultEvolutionPropertyValues.GENE_DELETION_RATE))),
          Double.parseDouble(props.getProperty("gene.addition.rate", String.valueOf(DefaultEvolutionPropertyValues.GENE_ADDITION_RATE))),
          Boolean.parseBoolean(props.getProperty("reflectors.enabled", String.valueOf(DefaultEvolutionPropertyValues.REFLECTORS_ENABLED))),
          Boolean.parseBoolean(props.getProperty("benders.enabled", String.valueOf(DefaultEvolutionPropertyValues.BENDERS_ENABLED))),
          Boolean.parseBoolean(props.getProperty("accelerators.enabled", String.valueOf(DefaultEvolutionPropertyValues.ACCELERATORS_ENABLED))),
          Boolean.parseBoolean(props.getProperty("repellents.enabled", String.valueOf(DefaultEvolutionPropertyValues.REPELLENTS_ENABLED)))
      );
    } catch (IOException e) {
      log.error("Can't read properties " + propertiesFile, e);
      throw new RuntimeException(e);
    }

    return properties;
  }

  public EvolutionProperties createDefaultEvolutionProperties() {
    return new EvolutionProperties(
        DefaultEvolutionPropertyValues.ELITES,
        DefaultEvolutionPropertyValues.CONCURRENT,
        DefaultEvolutionPropertyValues.TARGET_FITNESS,
        DefaultEvolutionPropertyValues.KEEP_MACHINE_ALIVE,
        DefaultEvolutionPropertyValues.GENE_MUTATION_RATE,
        DefaultEvolutionPropertyValues.GENE_ADDITION_RATE,
        DefaultEvolutionPropertyValues.GENE_DELETION_RATE,
        DefaultEvolutionPropertyValues.REFLECTORS_ENABLED,
        DefaultEvolutionPropertyValues.BENDERS_ENABLED,
        DefaultEvolutionPropertyValues.ACCELERATORS_ENABLED,
        DefaultEvolutionPropertyValues.REPELLENTS_ENABLED
    );
  }


}
