package ee.joonasvali.mirrors.scene.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Genome extends ArrayList<Gene> {

  public Genome() {
  }

  public Genome(List<Gene> list) {
    super(list);
  }

  public List<Gene> getGenes() {
    return Collections.unmodifiableList(this);
  }

  public Genome copy() {
    return stream().map(Gene::copy).collect(Collectors.toCollection(Genome::new));
  }

  public Genome getOffspring(GeneFactory geneFactory) {
    List<Gene> genome = new ArrayList<>(this.size());
    for (Gene gene : this) {
      genome.add(geneFactory.getOffspringGene(gene));
    }
    return new Genome(genome);
  }
}
