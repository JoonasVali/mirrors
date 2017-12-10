package ee.joonasvali.mirrors.scene.genetic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genepool extends ArrayList<Gene> {

  public Genepool() { }

  public Genepool(List<Gene> list) {
    super(list);
  }

  public List<Gene> getGenes() {
    return Collections.unmodifiableList(this);
  }

  public Genepool getOffspring(GeneFactory geneFactory) {
    List<Gene> pool = new ArrayList<>(this.size());
    for(Gene gene : this) {
      pool.add(gene.getOffspringGene(geneFactory));
    }
    return new Genepool(pool);
  }
}
