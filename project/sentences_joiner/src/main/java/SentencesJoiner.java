import org.jbpt.algo.tree.rpst.IRPSTNode;
import org.jbpt.algo.tree.rpst.RPST;
import org.jbpt.graph.DirectedEdge;
import org.jbpt.graph.MultiDirectedGraph;
import org.jbpt.graph.abs.IFragment;
import org.jbpt.hypergraph.abs.Vertex;

public class SentencesJoiner {
    private MultiDirectedGraph graph;
    private RPST<DirectedEdge, Vertex> rpst;

    private String joinedSentences;

    public SentencesJoiner(MultiDirectedGraph graph) {
        this.graph = graph;
        this.rpst = new RPST<>(graph);
        this.joinSentences();
    }

    public String getJoinedSentences() {
        return joinedSentences;
    }

    private void joinSentences() {
        IRPSTNode<DirectedEdge, Vertex> root = rpst.getRoot();
        if (root.getEntry() == null) {
            System.out.println("ROOT:" + root);
        }
        IFragment<DirectedEdge, Vertex> fragment = root.getFragment();
//        ElementVertex entry = (ElementVertex) root.getEntry();
//        System.out.println(entry.getSentence());
        this.joinedSentences = "";
    }
}
