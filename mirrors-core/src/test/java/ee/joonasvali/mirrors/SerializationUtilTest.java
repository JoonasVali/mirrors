package ee.joonasvali.mirrors;

import com.google.gson.Gson;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import ee.joonasvali.mirrors.scene.genetic.impl.AcceleratorGene;
import ee.joonasvali.mirrors.scene.genetic.impl.BenderGene;
import ee.joonasvali.mirrors.scene.genetic.util.SerializationUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SerializationUtilTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void singleGeneSerializedAndDeserialized() throws IOException {
    Path temp = folder.newFolder("mirrors-test").toPath();
    SerializationUtil util = new SerializationUtil(temp.resolve("test-evolution"));
    Genome genome = new Genome(Collections.singletonList(new BenderGene(50, 60, 70, 80)));
    util.serialize(genome, "abc");
    Path file = temp.resolve("test-evolution").resolve("abc.json");
    Genome result = SerializationUtil.deserialize(file);
    BenderGene bender = (BenderGene) result.get(0);
    boolean equals = EqualsBuilder.reflectionEquals(bender, new BenderGene(50, 60, 70, 80));
    Assert.assertTrue("Bender gene not equal to expected bender gene", equals);
  }

  @Test
  public void multipleGenesSerializedAndDeserialized() throws IOException {
    Path temp = folder.newFolder("mirrors-test").toPath();
    SerializationUtil util = new SerializationUtil(temp.resolve("test-evolution"));
    List<Gene> geneList = new ArrayList<>();
    geneList.add(new BenderGene(50, 60, 70, 80));
    geneList.add(new BenderGene(150, 160, 170, 180));
    geneList.add(new AcceleratorGene(1, 2, 3, 4));
    Genome genome = new Genome(geneList);
    util.serialize(genome, "abc");

    Path file = temp.resolve("test-evolution").resolve("abc.json");
    Genome result = SerializationUtil.deserialize(file);
    Assert.assertEquals(3, result.size());

    assertAllElementsPresentByFieldComparison(geneList, result);
  }

  @Test
  public void serializePopulation() throws IOException {
    Path temp = folder.newFolder("mirrors-test").toPath();
    Path evolutionDir = temp.resolve("test-evolution");
    Files.createDirectories(evolutionDir);

    Path evolutionFile = evolutionDir.resolve("evo.json");
    SerializationUtil util = new SerializationUtil(evolutionDir);
    Genome genes1 = new Genome(Collections.singletonList(new BenderGene(50, 60, 70, 80)));
    Genome genes2 = new Genome(Collections.singletonList(new BenderGene(51, 61, 71, 81)));
    ArrayList<Genome> population = new ArrayList<>();
    population.add(genes1);
    population.add(genes2);

    util.serializePopulation(population, evolutionFile);

    String expected = "[\n" +
        "  {\n" +
        "    \"ee.joonasvali.mirrors.scene.genetic.impl.BenderGene\": [\n" +
        "      {\n" +
        "        \"x\": 50.0,\n" +
        "        \"y\": 60.0,\n" +
        "        \"radius\": 70.0,\n" +
        "        \"strength\": 80.0\n" +
        "      }\n" +
        "    ]\n" +
        "  },\n" +
        "  {\n" +
        "    \"ee.joonasvali.mirrors.scene.genetic.impl.BenderGene\": [\n" +
        "      {\n" +
        "        \"x\": 51.0,\n" +
        "        \"y\": 61.0,\n" +
        "        \"radius\": 71.0,\n" +
        "        \"strength\": 81.0\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "]";

    String contents = new String(Files.readAllBytes(evolutionFile), StandardCharsets.UTF_8);
    Assert.assertEquals(expected, contents);
  }

  @Test
  public void deserializePopulation() throws IOException {
    Path temp = folder.newFolder("mirrors-test").toPath();
    Path evolutionDir = temp.resolve("test-evolution");
    Files.createDirectories(evolutionDir);

    Path evolutionFile = evolutionDir.resolve("evo.json");
    String content = "[\n" +
        "  {\n" +
        "    \"ee.joonasvali.mirrors.scene.genetic.impl.BenderGene\": [\n" +
        "      {\n" +
        "        \"x\": 50.0,\n" +
        "        \"y\": 60.0,\n" +
        "        \"radius\": 70.0,\n" +
        "        \"strength\": 80.0\n" +
        "      }\n" +
        "    ]\n" +
        "  },\n" +
        "  {\n" +
        "    \"ee.joonasvali.mirrors.scene.genetic.impl.BenderGene\": [\n" +
        "      {\n" +
        "        \"x\": 51.0,\n" +
        "        \"y\": 61.0,\n" +
        "        \"radius\": 71.0,\n" +
        "        \"strength\": 81.0\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "]";
    Files.write(evolutionFile, content.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE_NEW);
    Collection<Genome> result = SerializationUtil.deserializePopulation(evolutionFile);

    Genome genes1 = new Genome(Collections.singletonList(new BenderGene(50, 60, 70, 80)));
    Genome genes2 = new Genome(Collections.singletonList(new BenderGene(51, 61, 71, 81)));
    ArrayList<Genome> expectedPopulation = new ArrayList<>();
    expectedPopulation.add(genes1);
    expectedPopulation.add(genes2);

    assertAllElementsPresentByFieldComparison(expectedPopulation, result);
  }

  @Test
  public void overwritesExistingPopulationFile() throws IOException {
    Path temp = folder.newFolder("mirrors-test").toPath();
    Path evolutionDir = temp.resolve("test-evolution");
    Files.createDirectories(evolutionDir);

    Path evolutionFile = evolutionDir.resolve("evo.json");
    String existingPopulation = "[\n" +
        "  {\n" +
        "    \"ee.joonasvali.mirrors.scene.genetic.impl.BenderGene\": [\n" +
        "      {\n" +
        "        \"x\": 50.0,\n" +
        "        \"y\": 60.0,\n" +
        "        \"radius\": 70.0,\n" +
        "        \"strength\": 80.0\n" +
        "      }\n" +
        "    ]\n" +
        "  },\n" +
        "  {\n" +
        "    \"ee.joonasvali.mirrors.scene.genetic.impl.BenderGene\": [\n" +
        "      {\n" +
        "        \"x\": 51.0,\n" +
        "        \"y\": 61.0,\n" +
        "        \"radius\": 71.0,\n" +
        "        \"strength\": 81.0\n" +
        "      }\n" +
        "    ]\n" +
        "  }\n" +
        "]";
    Files.write(evolutionFile, existingPopulation.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);

    SerializationUtil util = new SerializationUtil(evolutionDir);
    Genome genes1 = new Genome(Collections.singletonList(new BenderGene(11, 12, 13, 14)));
    ArrayList<Genome> population = new ArrayList<>();
    population.add(genes1);
    util.serializePopulation(population, evolutionFile);
    Collection<Genome> result = SerializationUtil.deserializePopulation(evolutionFile);

    assertAllElementsPresentByFieldComparison(population, result);
  }

  private <T> void assertAllElementsPresentByFieldComparison(Collection<T> expected, Collection<T> actual) {
    List<T> expectedCopy = new ArrayList<>(expected);
    // The order isn't guaranteed so checking for presence of every gene is a bit complicated:
    for (T obj : actual) {
      Iterator<T> iterator = expectedCopy.iterator();
      while (iterator.hasNext()) {
        T candidate = iterator.next();
        if (EqualsBuilder.reflectionEquals(candidate, obj)) {
          iterator.remove();
          break;
        }
      }
    }

    if (!expectedCopy.isEmpty()) {
      throw new Error("Unable to find objects from provided data: " + new Gson().toJson(expectedCopy));
    }
  }
}
