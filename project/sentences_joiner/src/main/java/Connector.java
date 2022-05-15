import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

public abstract class Connector {
    protected final String separator = "-";
    protected String selectedConnector;
    protected ArrayList<String> connectors;
    protected final NLGFactory nlgFactory;
    protected final boolean randomConnector;
    protected int currentIndex;

    public Connector() {
        Lexicon lexicon = Lexicon.getDefaultLexicon();
        this.nlgFactory = new NLGFactory(lexicon);

        randomConnector = false;
        currentIndex = 0;
        selectConnector();
    }

    private void selectConnector() {
        if (!randomConnector) {
            selectConnector(currentIndex);
        }
        else {
            selectRandomConnector();
        }
    }

    protected void selectConnector(int index) {
        int indexModule = index % connectors.size();
        selectedConnector = connectors.get(indexModule);
    }

    public void printConnectors() {
        connectors.forEach(System.out::println);
    }

    public String getSelectedConnector() {
        return selectedConnector;
    }

    public ArrayList<String> getConnectors() {
        return connectors;
    }

    protected void addConnectors(String connector) {
        connectors.add(connector);
    }

    protected void addConnectors(Collection<String> connectors) {
        this.connectors.addAll(connectors);
    }

    protected void selectRandomConnector() {
        Random rand = new Random();

        int indexModule = rand.nextInt(connectors.size());
        selectedConnector = connectors.get(indexModule);
    }

    public Connector(String connector) {
        Lexicon lexicon = Lexicon.getDefaultLexicon();
        this.nlgFactory = new NLGFactory(lexicon);

        connectors.add(connector);
        randomConnector = false;
        currentIndex = 0;
        selectConnector();
    }

    protected void incrementIndex() {
        currentIndex++;
    }

    protected String[] splitConnector(String connector) {
        return connector.split(separator);
    }

    abstract public void transformSentenceWithConnector(Sentence sentence);

    public Connector(Collection<String> connectors) {
        Lexicon lexicon = Lexicon.getDefaultLexicon();
        this.nlgFactory = new NLGFactory(lexicon);

        this.connectors.addAll(connectors);
        randomConnector = false;
        selectConnector();
    }
}
