import org.apache.commons.lang3.tuple.Pair;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;

import java.util.ArrayList;
import java.util.Map;

public class SentencesJoiner {
    private final NLGFactory nlgFactory;

    public SentencesJoiner() {
        Lexicon lexicon = Lexicon.getDefaultLexicon();
        this.nlgFactory = new NLGFactory(lexicon);
    }

    public Sentence joinSentences(ElementVertex vertex, ArrayList<Sentence> sentences) {
        sentences.removeIf(this::sentenceIsVoid);
        if (sentences.size() == 0) return new Sentence();

        if (joiningBranches(vertex, sentences)) {
            return joinBranches(vertex, sentences);
        }

        if (vertexIsFirstGateway(sentences)) {
            return joinGateways(vertex, sentences);
        }

        return joinActivities(sentences);
    }

    private boolean sentenceIsVoid(Sentence sentence) {
        return sentence == null || sentence.isVoid();
    }

    private boolean joiningBranches(ElementVertex vertex, ArrayList<Sentence> sentences) {
        if (!vertex.isBifurcation()) return false;

        boolean joiningBranches = true;
        ArrayList<String> nextIds = vertex.getNextIds();

        for (String id : nextIds) {
            boolean coincidenceId = false;
            for (Sentence sentence : sentences) {
                if (id.equals(sentence.getIdOfFirstJoinedVertex())) {
                    coincidenceId = true;
                    break;
                }
            }
            if (!coincidenceId) {
                joiningBranches = false;
                break;
            }
        }

        return joiningBranches;
    }

    private boolean vertexIsFirstGateway(ArrayList<Sentence> sentences) {
        return sentences.get(0).isFirstGateway();
    }

    private Sentence joinBranches(ElementVertex vertex, ArrayList<Sentence> sentences) {
        Sentence coordinatedSentence = new Sentence();
        addNameToBranches(vertex.getNext(), sentences);
        addBranchesToSentence(sentences, coordinatedSentence);

        return coordinatedSentence;
    }

    private void addBranchesToSentence(ArrayList<Sentence> branches, Sentence coordinatedSentence) {
        for (Sentence sentence : branches) {
            coordinatedSentence.joinSentence(sentence, true);
        }
    }

    private Sentence joinGateways(ElementVertex gateway, ArrayList<Sentence> sentences) {
        Sentence coordinatedSentence = new Sentence();

        String sentenceString = sentences.get(0).paragraphToString();
        sentenceString = "the condition " + sentenceString + " is checked";
        NLGElement firstPhrase = nlgFactory.createSentence(sentenceString);
        Sentence firstSentence = new Sentence(firstPhrase, gateway);
        coordinatedSentence.joinSentence(firstSentence, false);
        sentences.remove(0);

        addSentencesToSentence(sentences, coordinatedSentence);

        return coordinatedSentence;
    }

    private void addSentencesToSentence(ArrayList<Sentence> sentences, Sentence coordinatedSentence) {
        sentences.forEach(sentence -> coordinatedSentence.joinSentence(sentence, false));
    }

    private Sentence joinActivities(ArrayList<Sentence> sentences) {
        Sentence coordinatedSentence = new Sentence();

        addSentencesToSentence(sentences, coordinatedSentence);

        return coordinatedSentence;
    }

    private void addNameToBranches(Map<String, Pair<String, Boolean>> next, ArrayList<Sentence> sentences) {
        if (next.size() != sentences.size()) {
            System.out.println("ERROR: Names size different than sentences size");
            System.out.println("    " + next);
            System.out.println("    " + sentences);
            System.out.print("    [");
            sentences.forEach(Sentence::printParagraph);
            System.out.println("]");
            return;
        }

        for (Sentence sentence : sentences) {
            String elementId = sentence.getIdOfFirstJoinedVertex();
            String name = next.get(elementId).getKey();
            String sentenceString = sentence.paragraphToString();
            sentenceString = "If the answer is " + name + " then " + sentenceString;
            NLGElement branchPhrase = nlgFactory.createSentence(sentenceString);
            sentence.setCoordinatedPhrase(branchPhrase);
        }
    }

}
