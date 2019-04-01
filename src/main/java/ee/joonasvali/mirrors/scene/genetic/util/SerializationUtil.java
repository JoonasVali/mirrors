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
import ee.joonasvali.mirrors.scene.genetic.Genepool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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
    builder.registerTypeAdapter(Genepool.class, new GenepoolAdapter());
    return builder.create();
  }

  public SerializationUtil(Path dir) {
    this.dir = dir;
  }

  public void serializePopulation(Collection<Genepool> pools, Path file) {
    try {
      savePopulation(pools, file);
    } catch (IOException e) {
      log.error("abc", e);
    }
    log.info("saved population to " + file.toString());
  }

  public void serialize(Genepool pool, String name) throws IOException {
    Files.createDirectories(dir);
    Path file = dir.resolve(name + ".json");
    try {
      save(pool, file);
    } catch (IOException e) {
      log.error("abc", e);
    }
    log.info("saved pool to " + file.toString());
  }

  private void save(Genepool pool, Path path) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      gson.toJson(pool, writer);
    }
  }

  private void savePopulation(Collection<Genepool> pools, Path path) throws IOException {
    try (BufferedWriter writer = Files.newBufferedWriter(path)) {
      gson.toJson(pools, writer);
    }
  }

  public static Genepool deserialize(Path file) throws IOException {
    String contents = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
    return gson.fromJson(contents, Genepool.class);
  }

  public static Collection<Genepool> deserializePopulation(Path file) throws IOException {
    String contents = new String(Files.readAllBytes(file), StandardCharsets.UTF_8);
    Type collectionType = new TypeToken<Collection<Genepool>>() {}.getType();
    return gson.fromJson(contents, collectionType);
  }

  private static class GenepoolAdapter implements JsonSerializer<Genepool>, JsonDeserializer<Genepool> {
    public Genepool deserialize(JsonElement jsonElement, Type type,
                         JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
      JsonObject jsonObject = jsonElement.getAsJsonObject();
      List<Gene> genes = new ArrayList<>();
      for (String key: jsonObject.keySet()) {
        Class klass = getGeneClass(key);

        JsonArray array = jsonObject.get(key).getAsJsonArray();
        for (JsonElement element : array) {
          Gene gene = jsonDeserializationContext.deserialize(element, klass);
          genes.add(gene);
        }
      }
      return new Genepool(genes);
    }

    public JsonElement serialize(Genepool pool, Type type, JsonSerializationContext jsonSerializationContext) {
      Map<String, List<Gene>> genemap = new HashMap<>();
      pool.forEach(gene -> {
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