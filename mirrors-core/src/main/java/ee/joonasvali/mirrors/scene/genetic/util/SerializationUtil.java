package ee.joonasvali.mirrors.scene.genetic.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;
import ee.joonasvali.mirrors.scene.genetic.Gene;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SerializationUtil {
  private static Logger log = LoggerFactory.getLogger(SerializationUtil.class);
  private final Path dir;
  private static Gson gson = createGson();

  private static Gson createGson() {
    GsonBuilder builder = new GsonBuilder().setPrettyPrinting();
    builder.registerTypeAdapter(Genome.class, new GenomeAdapter());
    return builder.create();
  }

  public SerializationUtil(Path dir) {
    this.dir = dir;
  }

  public void serializePopulation(Collection<Genome> genepool, Path file) {
    try {
      savePopulation(genepool, file);
    } catch (IOException e) {
      throw new RuntimeException("Can't create file or write into it: " + file, e);
    }
    log.info("saved population to " + file.toString());
  }

  public void serialize(Genome genome, String name) throws IOException {
    Files.createDirectories(dir);
    Path file = dir.resolve(name + ".json");
    try {
      save(genome, file);
      log.info("saved genome to " + file.toString());
    } catch (IOException e) {
      log.error("Unable to serialize genome.", e);
    }
  }

  private void save(Genome genome, Path path) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      gson.toJson(genome, writer);
    }
  }

  private void savePopulation(Collection<Genome> genepool, Path path) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING)) {
      gson.toJson(genepool, writer);
    }
  }

  public static Genome deserialize(Path file) throws IOException {
    String contents = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
    return gson.fromJson(contents, Genome.class);
  }

  public static Collection<Genome> deserializePopulation(Path file) throws IOException {
    String contents = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
    Type collectionType = new TypeToken<Collection<Genome>>() {
    }.getType();
    return gson.fromJson(contents, collectionType);
  }

  private static class GenomeAdapter implements JsonSerializer<Genome>, JsonDeserializer<Genome> {
    public Genome deserialize(JsonElement jsonElement, Type type,
                              JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      List<Gene> genes = new ArrayList<>();
      for (String key : jsonObject.keySet()) {
        Class klass = getGeneClass(key);

        JsonArray array = jsonObject.get(key).getAsJsonArray();
        for (JsonElement element : array) {
          Gene gene = jsonDeserializationContext.deserialize(element, klass);
          genes.add(gene);
        }
      }
      return new Genome(genes);
    }

    public JsonElement serialize(Genome genome, Type type, JsonSerializationContext jsonSerializationContext) {
      Map<String, List<Gene>> genemap = new HashMap<>();
      genome.forEach(gene -> {
        List<Gene> list = genemap.computeIfAbsent(gene.getClass().getName(), klass -> new ArrayList<>());
        list.add(gene);
      });

      JsonObject jsonObject = new JsonObject();
      genemap.forEach((k, v) -> jsonObject.add(k, jsonSerializationContext.serialize(v)));
      return jsonObject;
    }

    private Class getGeneClass(String className) {
      try {
        return Class.forName(className);
      } catch (ClassNotFoundException e) {
        throw new JsonParseException(e.getMessage());
      }
    }
  }
}