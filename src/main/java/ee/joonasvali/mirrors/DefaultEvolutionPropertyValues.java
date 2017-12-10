package ee.joonasvali.mirrors;

public interface DefaultEvolutionPropertyValues {
  int ELITES = 2;
  int CONCURRENT = 10;
  int TARGET_FITNESS = 350000;
  boolean KEEP_MACHINE_ALIVE = false;
  double GENE_MUTATION_RATE = 0.1d;
  boolean REFLECTORS_ENABLED = true;
  boolean BENDERS_ENABLED = true;
  boolean ACCELERATORS_ENABLED = true;
  boolean REPELLENTS_ENABLED = true;
  double GENE_ADDITION_RATE = 0.05;
  double GENE_DELETION_RATE = 0.01;
}
