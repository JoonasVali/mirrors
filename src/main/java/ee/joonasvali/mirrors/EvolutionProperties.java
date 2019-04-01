package ee.joonasvali.mirrors;


public class EvolutionProperties {
  private final int elites;
  private final int concurrent;
  private final int targetFitness;
  private final boolean keepAlive;
  private final boolean reflectorsEnabled;
  private final boolean bendersEnabled;
  private final boolean acceleratorsEnabled;
  private final boolean repellentsEnabled;
  private final double mutationRate;
  private final double geneAdditionRate;
  private final double geneDeletionRate;

  public EvolutionProperties(
      int elites,
      int concurrent,
      int targetFitness,
      boolean keepAlive,
      double mutationRate,
      double geneAdditionRate,
      double geneDeletionRate,
      boolean reflectorsEnabled,
      boolean bendersEnabled,
      boolean acceleratorsEnabled,
      boolean repellentsEnabled) {
    this.elites = elites;
    this.concurrent = concurrent;
    this.keepAlive = keepAlive;
    this.targetFitness = targetFitness;
    this.mutationRate = mutationRate;
    this.geneAdditionRate = geneAdditionRate;
    this.geneDeletionRate = geneDeletionRate;
    this.reflectorsEnabled = reflectorsEnabled;
    this.bendersEnabled = bendersEnabled;
    this.acceleratorsEnabled = acceleratorsEnabled;
    this.repellentsEnabled = repellentsEnabled;
  }

  public int getElites() {
    return elites;
  }

  public int getConcurrent() {
    return concurrent;
  }

  public int getTargetFitness() {
    return targetFitness;
  }

  public boolean isKeepAlive() {
    return keepAlive;
  }

  public boolean isReflectorsEnabled() {
    return reflectorsEnabled;
  }

  public boolean isBendersEnabled() {
    return bendersEnabled;
  }

  public boolean isAcceleratorsEnabled() {
    return acceleratorsEnabled;
  }

  public boolean isRepellentsEnabled() {
    return repellentsEnabled;
  }

  public double getMutationRate() {
    return mutationRate;
  }

  public double getGeneAdditionRate() {
    return geneAdditionRate;
  }

  public double getGeneDeletionRate() {
    return geneDeletionRate;
  }
}
