package ee.joonasvali.mirrors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.function.Function;

import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.ACCELERATORS_ENABLED;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.BENDERS_ENABLED;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.BOTTOM_PRODUCER_ENABLED;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.CONCURRENT;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.ELITES;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.GENE_ADDITION_RATE;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.GENE_DELETION_RATE;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.GENE_MUTATION_RATE;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.KEEP_MACHINE_ALIVE;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.MIDDLE_PRODUCER_ENABLED;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.REFLECTORS_ENABLED;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.REPELLENTS_ENABLED;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.TARGET_FITNESS;
import static ee.joonasvali.mirrors.DefaultEvolutionPropertyValues.TOP_PRODUCER_ENABLED;

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
          getProperty(props,"elites", Integer::parseInt, ELITES),
          getProperty(props,"concurrent", Integer::parseInt, CONCURRENT),
          getProperty(props,"target.fitness", Integer::parseInt, TARGET_FITNESS),
          getProperty(props,"keep.machine.alive", Boolean::parseBoolean, KEEP_MACHINE_ALIVE),
          getProperty(props,"gene.mutation.rate", Double::parseDouble, GENE_MUTATION_RATE),
          getProperty(props,"gene.deletion.rate", Double::parseDouble, GENE_DELETION_RATE),
          getProperty(props,"gene.addition.rate", Double::parseDouble, GENE_ADDITION_RATE),
          getProperty(props,"reflectors.enabled", Boolean::parseBoolean, REFLECTORS_ENABLED),
          getProperty(props,"benders.enabled", Boolean::parseBoolean, BENDERS_ENABLED),
          getProperty(props,"accelerators.enabled", Boolean::parseBoolean, ACCELERATORS_ENABLED),
          getProperty(props,"repellents.enabled", Boolean::parseBoolean, REPELLENTS_ENABLED)
      );

      boolean topProducerEnabled = getProperty(
          props,"top.producer.enabled", Boolean::parseBoolean, TOP_PRODUCER_ENABLED
      );
      boolean middleProducerEnabled = getProperty(
          props,"middle.producer.enabled", Boolean::parseBoolean, MIDDLE_PRODUCER_ENABLED
      );
      boolean bottomProducerEnabled = getProperty(
          props,"bottom.producer.enabled", Boolean::parseBoolean, BOTTOM_PRODUCER_ENABLED
      );

      properties.setTopProducerEnabled(topProducerEnabled);
      properties.setMiddleProducerEnabled(middleProducerEnabled);
      properties.setBottomProducerEnabled(bottomProducerEnabled);
    } catch (IOException e) {
      log.error("Can't read properties " + propertiesFile, e);
      throw new RuntimeException(e);
    }

    return properties;
  }

  private <T> T getProperty(Properties props, String key, Function<String, T> parseFunction, T defaultVal) {
    String value = props.getProperty(key);
    if (value == null) {
      return defaultVal;
    }
    try {
      return parseFunction.apply(value);
    } catch (Exception e) {
      log.error("Unable to parse value " + value + " for key " + key, e);
      return defaultVal;
    }
  }


}
