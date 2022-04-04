import org.jbpt.algo.tree.tctree.TCType;
import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.util.ArrayList;
import java.util.Objects;

public class SentencesJoiner {
    Lexicon lexicon;
    NLGFactory nlgFactory;
    Realiser realiser;

    private final String commaJoin = ", ";
    private final String dotJoin = ". ";

    public SentencesJoiner() {
        this.lexicon = Lexicon.getDefaultLexicon();
        this.nlgFactory = new NLGFactory(lexicon);
        this.realiser = new Realiser(lexicon);
    }

    public String sentenceToString(NLGElement sentence) {
        return realizeSentence(sentence);
    }

    private String realizeSentence(NLGElement sentence) {
        try {
            return realiser.realiseSentence(sentence);
        } catch (Exception e) {
            return "";
        }
    }

    public NLGElement joinSentences(TCType nodeType, ArrayList<NLGElement> sentences) {
        CoordinatedPhraseElement coordinatedPhrase = nlgFactory.createCoordinatedPhrase();
        coordinatedPhrase.setConjunction("then");
        sentences.removeIf(this::sentenceIsVoid);
        addCoordinateSentence(coordinatedPhrase, sentences.get(0));
        sentences.remove(0);
        for (NLGElement sentence : sentences) {
            int totalWordCount = countWords(sentence) + countWords(coordinatedPhrase);
            if (totalWordCount < 50) {
                addCoordinateSentence(coordinatedPhrase, sentence);
            }
            else {
                addCoordinateSentence(coordinatedPhrase, sentence);
            }
        }
        return coordinatedPhrase;
    }

    private boolean sentenceIsVoid(NLGElement sentence) {
        return sentence == null || realizeSentence(sentence).equals("");
    }

    private void addCoordinateSentence(CoordinatedPhraseElement coordinatedPhrase, NLGElement sentence) {
        if (sentence instanceof SPhraseSpec) {
            coordinatedPhrase.addCoordinate(sentence);
        }
        else {
            String removedDotSentence = realizeSentence(sentence).replace(".", "");
            coordinatedPhrase.addCoordinate(removedDotSentence);
        }
    }

    private int countWords(NLGElement element) {
        String sentence = realizeSentence(element);
        if (sentence.isEmpty()) {
            return 0;
        }

        String[] words = sentence.split("\\s+");
        return words.length;
    }

//    public NLGElement joinSentences(TCType nodeType, ArrayList<NLGElement> sentences) {
//        NLGElement joinedSentence = null;
//        for (NLGElement sentence : sentences) {
//
//        }
//        return joinedSentence;
//    }

    public NLGElement joinGateways(String gatewayType, ArrayList<NLGElement> sentences) {
        CoordinatedPhraseElement coordinatedPhrase = nlgFactory.createCoordinatedPhrase();
        sentences.removeIf(Objects::isNull);
        NLGElement firstSentence = sentences.get(0);
        coordinatedPhrase.addCoordinate(firstSentence);
        sentences.remove(0);
        return coordinatedPhrase;
    }
}
