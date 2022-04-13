import simplenlg.framework.CoordinatedPhraseElement;
import simplenlg.framework.NLGElement;
import simplenlg.framework.NLGFactory;
import simplenlg.lexicon.Lexicon;
import simplenlg.phrasespec.SPhraseSpec;
import simplenlg.realiser.english.Realiser;

import java.util.ArrayList;

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

    public NLGElement joinSentences(ElementVertex vertex, ArrayList<NLGElement> sentences) {
        if (vertexIsFirstGateway(vertex, sentences)) {
            return joinGateways(vertex, sentences);
        }
        return joinActivities(vertex.getType(), sentences);
    }

    private boolean vertexIsFirstGateway(ElementVertex vertex, ArrayList<NLGElement> sentences) {
        return vertex.isOpenGateway() && vertex.getPhrase().equals(sentences.get(0));
    }

    private NLGElement joinGateways(ElementVertex gateway, ArrayList<NLGElement> sentences) {
        sentences.removeIf(this::sentenceIsVoid);
        if (sentences.size() == 0) return null;

        CoordinatedPhraseElement coordinatedPhrase = nlgFactory.createCoordinatedPhrase();
        String firstSentence = realiser.realiseSentence(sentences.get(0));
        firstSentence = "the condition " + firstSentence + " is checked";
        NLGElement firstPhrase = nlgFactory.createSentence(firstSentence);
        addCoordinateSentence(coordinatedPhrase, firstPhrase);
        sentences.remove(0);

        coordinatedPhrase.setConjunction("then");
        for (NLGElement sentence : sentences) {
            int totalWordCount = countWords(sentence) + countWords(coordinatedPhrase);
            if (totalWordCount < 50) {
                addCoordinateSentence(coordinatedPhrase, sentence);
            }
            else {
                // TODO Treatment for long sentences
                addCoordinateSentence(coordinatedPhrase, sentence);
            }
        }
        return coordinatedPhrase;
    }

    private NLGElement joinActivities(String type, ArrayList<NLGElement> sentences) {
        CoordinatedPhraseElement coordinatedPhrase = nlgFactory.createCoordinatedPhrase();
        coordinatedPhrase.setConjunction("then");
        sentences.removeIf(this::sentenceIsVoid);
        if (sentences.size() == 0) return null;

        addCoordinateSentence(coordinatedPhrase, sentences.get(0));
        sentences.remove(0);
        for (NLGElement sentence : sentences) {
            int totalWordCount = countWords(sentence) + countWords(coordinatedPhrase);
            if (totalWordCount < 50) {
                addCoordinateSentence(coordinatedPhrase, sentence);
            }
            else {
                // TODO Treatment for long sentences
                addCoordinateSentence(coordinatedPhrase, sentence);
            }
        }
        return coordinatedPhrase;
    }

    private boolean sentenceIsVoid(NLGElement sentence) {
        return sentence == null || realizeSentence(sentence).equals("");
    }

    private void addCoordinateSentence(CoordinatedPhraseElement coordinatedPhrase, NLGElement sentence) {
        String realizedSentence = realizeSentence(sentence);
        if (Character.isUpperCase(realizedSentence.charAt(0))) {
            realizedSentence = realizedSentence.toLowerCase();
        }
        if (sentence instanceof SPhraseSpec) {
            coordinatedPhrase.addCoordinate(sentence);
        }
        else {
            String removedDotSentence = realizedSentence.replace(".", "");
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
}
