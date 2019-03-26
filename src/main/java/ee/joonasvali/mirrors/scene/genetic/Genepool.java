package ee.joonasvali.mirrors.scene.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Genepool extends ArrayList<Gene> {

  public Genepool() { }

  public Genepool(List<Gene> list) {
    super(list);
  }

  public List<Gene> getGenes() {
    return Collections.unmodifiableList(this);
  }

  public Genepool copy() {
    return stream().map(Gene::copy).collect(Collectors.toCollection(Genepool::new));
  }

  public Genepool getOffspring(GeneFactory geneFactory) {
    List<Gene> pool = new ArrayList<>(this.size());
    for(Gene gene : this) {
      pool.add(gene.getOffspringGene(geneFactory));
    }
    return new Genepool(pool);
  }
}
