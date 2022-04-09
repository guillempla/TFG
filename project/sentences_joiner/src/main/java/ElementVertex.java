import org.apache.commons.lang3.tuple.Pair;
import org.jbpt.hypergraph.abs.Vertex;
import simplenlg.framework.NLGElement;

import java.util.ArrayList;

public class ElementVertex extends Vertex {
    // Inspired in https://github.com/pawelgalka/pm_split_miner/blob/5c140f1d31303a5afc0337c481c80568f10d6416/java-joiner/src/main/java/pl/edu/agh/Graph.java
    private final String sentence;
    private final NLGElement phrase;
    private final String type;
    private boolean visited;
    private final ArrayList<Pair<String, String>> next;

    public ElementVertex(String id, String sentence, NLGElement phrase, String type, ArrayList<Pair<String, String>> next) {
        super(id);
        this.sentence = sentence;
        this.phrase = phrase;
        this.type = type;
        this.visited = false;
        this.next = next;
    }

    public ArrayList<Pair<String, String>> getNext() {
        return this.next;
    }

    public ArrayList<String> getNextIds() {
        ArrayList<String> nextIds = new ArrayList<>();
        for (Pair<String, String> pair : this.next) {
            nextIds.add(pair.getKey());
        }
        return nextIds;
    }

    public Boolean isOpenGateway() {
        return isGateway() && next.size() > 1;
    }

    public Boolean isGateway() {
        return this.type.toLowerCase().contains("gateway");
    }

    public Boolean isVisited() {
        return this.visited;
    }

    public String getElementId() {
        return super.getName();
    }

    public String getSentence() {
        return this.sentence;
    }

    public NLGElement getPhrase() {
        return this.phrase;
    }

    public String getType() {
        return this.type;
    }

    public void setVisited(Boolean visited) {
        this.visited = visited;
    }
}
