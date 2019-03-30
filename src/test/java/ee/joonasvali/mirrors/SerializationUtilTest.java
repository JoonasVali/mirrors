package ee.joonasvali.mirrors;

import com.google.gson.Gson;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import ee.joonasvali.mirrors.scene.genetic.impl.AcceleratorGene;
import ee.joonasvali.mirrors.scene.genetic.impl.BenderGene;
import ee.joonasvali.mirrors.scene.genetic.util.SerializationUtil;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class SerializationUtilTest {

  @Rule
  public TemporaryFolder folder = new TemporaryFolder();

  @Test
  public void singleGeneSerializedAndDeserialized() throws IOException {
    Path temp = folder.newFolder("mirrors-test").toPath();
    SerializationUtil util = new SerializationUtil(temp, "test-evolution");
    Genepool pool = new Genepool(Collections.singletonList(new BenderGene(50, 60, 70, 80)));
    util.serialize(pool, "abc");
    Path file = temp.resolve("test-evolution").resolve("abc.json");
    Genepool result = SerializationUtil.deserialize(file);
    BenderGene bender = (BenderGene) result.get(0);
    boolean equals = EqualsBuilder.reflectionEquals(bender, new BenderGene(50, 60, 70, 80));
    Assert.assertTrue("Bender gene not equal to expected bender gene", equals);
  }

  @Test
  public void multipleGenesSerializedAndDeserialized() throws IOException {
    Path temp = folder.newFolder("mirrors-test").toPath();
    SerializationUtil util = new SerializationUtil(temp, "test-evolution");
    List<Gene> geneList = new ArrayList<>();
    geneList.add(new BenderGene(50, 60, 70, 80));
    geneList.add(new BenderGene(150, 160, 170, 180));
    geneList.add(new AcceleratorGene(1, 2, 3, 4));
    Genepool pool = new Genepool(geneList);
    util.serialize(pool, "abc");

    Path file = temp.resolve("test-evolution").resolve("abc.json");
    Genepool result = SerializationUtil.deserialize(file);
    Assert.assertEquals(3, result.size());

    // The order isn't guaranteed so checking for presence of every gene is a bit complicated:
    for (Gene gene: result) {
      Iterator<Gene> expectedGenesIterator = geneList.iterator();
      while (expectedGenesIterator.hasNext()) {
        Gene candidate = expectedGenesIterator.next();
        if (EqualsBuilder.reflectionEquals(candidate, gene)) {
          expectedGenesIterator.remove();
          break;
        }
      }
    }
    if (!geneList.isEmpty()) {
      throw new Error("Didn't find genes from serialized data: " + new Gson().toJson(geneList));
    }
  }

}
