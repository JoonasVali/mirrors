package ee.joonasvali.mirrors.util;

import ee.joonasvali.mirrors.command.PopulationExplorer;
import ee.joonasvali.mirrors.scene.Constants;
import ee.joonasvali.mirrors.scene.genetic.Genome;
import ee.joonasvali.mirrors.scene.genetic.GenomeProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.stream.IntStream;

public class GenomeChooser extends JPanel implements GenomeProvider, ControlPanel {
  private static final Logger log = LoggerFactory.getLogger(PopulationExplorer.class);
  private final ArrayList<GenomeSelectionChangedListener> selectionChangedListenerList = new ArrayList<>();

  private volatile Genome genome;
  public static final int CONTROL_PANEL_WIDTH = 90;

  public GenomeChooser(ArrayList<Genome> list) {

    setLayout(new BorderLayout());
    add(new JLabel("Select genome"), BorderLayout.NORTH);
    String[] vals = IntStream.range(1, list.size()).mapToObj(String::valueOf).toArray(String[]::new);
    JList<String> jlist = new JList<>(vals);
    jlist.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
    jlist.setLayoutOrientation(JList.HORIZONTAL_WRAP);
    jlist.setVisibleRowCount(-1);
    jlist.setLayoutOrientation(JList.VERTICAL);

    jlist.addListSelectionListener(e -> {
      if (e.getValueIsAdjusting()) {
        return;
      }
      log.info("Selected: " + e.getFirstIndex());
      genome = list.get(e.getFirstIndex());
      selectionChangedListenerList.forEach(listener -> listener.changed(genome));
    });

    JScrollPane pane = new JScrollPane(jlist);
    pane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    add(pane, BorderLayout.CENTER);

    jlist.setMaximumSize(new Dimension(Constants.DIMENSION_X, Constants.DIMENSION_Y));
    genome = list.get(0);
  }

  @Override
  public Genome provide() {
    return genome;
  }

  public void addSelectionChangedListener(GenomeSelectionChangedListener listener) {
    selectionChangedListenerList.add(listener);
  }

  public void removeSelectionChangedListener(GenomeSelectionChangedListener listener) {
    selectionChangedListenerList.remove(listener);
  }

  public static void main(String[] args) throws InvocationTargetException, InterruptedException {
    ArrayList<Genome> list = new ArrayList<>();
    list.add(new Genome());
    list.add(new Genome());
    list.add(new Genome());
    list.add(new Genome());
    list.add(new Genome());
    list.add(new Genome());
    list.add(new Genome());
    list.add(new Genome());

    SwingUtilities.invokeAndWait(() -> {
      GenomeChooser g = new GenomeChooser(list);
      JFrame frame = new JFrame();
      frame.getContentPane().add(g);
      frame.setSize(new Dimension(CONTROL_PANEL_WIDTH, Constants.DIMENSION_Y));

      frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
      frame.setVisible(true);
    });
  }
}
