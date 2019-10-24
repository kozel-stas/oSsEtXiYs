package com.oSsEtXiYs.service.engine;

import com.oSsEtXiYs.service.model.Essay;
import com.oSsEtXiYs.service.model.Text;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class ClassicEssayService implements EssayService {

    private static final int MAX_SENTENCE_NUMBER = 10;

    private final ExternalEvaluativeService externalEvaluativeService;

    public ClassicEssayService(ExternalEvaluativeService externalEvaluativeService) {
        this.externalEvaluativeService = externalEvaluativeService;
    }

    @Override
    public Essay pressText(Text text) {
        Map<Text.Sentence, Double> essayCandidate = new HashMap<>();
        Map<String, Double> externalWeight = externalEvaluativeService.evaluateText(text);
        Map<String, Integer> textNumber = countTokens(text);
        int maxCount = Collections.max(textNumber.values());
        int numberOfSymbolsBeforeDocument = 0;
        int numberOfSymbolsBeforeParagraph = 0;
        for (Text.Paragraph paragraph : text.getParagraphs()) {
            for (Text.Sentence sentence : paragraph.getSentences()) {
                double posd = 1 - ((double) numberOfSymbolsBeforeDocument) / text.getCharacterNumber();
                double posp = 1 - ((double) numberOfSymbolsBeforeParagraph) / paragraph.getCharacterNumber();
                double sentenceScore = sentenceScore(sentence, textNumber, maxCount, externalWeight);
                essayCandidate.put(sentence, posd * posp * sentenceScore);
                numberOfSymbolsBeforeDocument += sentence.getCharacterNumber();
                numberOfSymbolsBeforeParagraph += sentence.getCharacterNumber();
            }
            numberOfSymbolsBeforeParagraph = 0;
        }
        return constructEssayFromCandidate(text, essayCandidate);
    }

    private double sentenceScore(Text.Sentence sentence, Map<String, Integer> documentTextNumber, int max, Map<String, Double> externalWeight) {
        double res = 1;
        Map<String, Integer> textNumber = new HashMap<>();
        countTokens(textNumber, sentence);
        for (String token : sentence.getTokens()) {
            res *= textNumber.get(token) * (0.5 * (1 + (double) documentTextNumber.get(token) / max)) * externalWeight.get(token);
        }
        return res;
    }

    private static Map<String, Integer> countTokens(Text text) {
        Map<String, Integer> textNumber = new HashMap<>();
        for (Text.Paragraph paragraph : text.getParagraphs()) {
            for (Text.Sentence sentence : paragraph.getSentences()) {
                countTokens(textNumber, sentence);
            }
        }
        return textNumber;
    }

    private static void countTokens(Map<String, Integer> textNumber, Text.Sentence sentence) {
        for (String word : sentence.getTokens()) {
            textNumber.computeIfPresent(word, (k, v) -> v + 1);
            textNumber.putIfAbsent(word, 1);
        }
    }

    private static Essay constructEssayFromCandidate(Text text, Map<Text.Sentence, Double> sentenceScore) {
        List<Text.Sentence> orderedSentences = new ArrayList<>();
        List<Double> result = new ArrayList<>(sentenceScore.values());
        Collections.sort(result);
        double max = result.get((Math.min(result.size(), MAX_SENTENCE_NUMBER) - 1));
        sentenceScore.values().removeIf(value -> value < max);
        for (Text.Paragraph paragraph : text.getParagraphs()) {
            for (Text.Sentence sentence : paragraph.getSentences()) {
                if (sentenceScore.containsKey(sentence)) {
                    orderedSentences.add(sentence);
                }
            }
        }
        return new Essay(Collections.singletonList(new Text.Paragraph(orderedSentences)));
    }

}
