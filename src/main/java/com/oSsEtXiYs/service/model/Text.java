package com.oSsEtXiYs.service.model;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Text {

    private final List<Paragraph> paragraphs;
    private final int characterNumber;

    public Text(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
        this.characterNumber = paragraphs.stream().mapToInt(Paragraph::getCharacterNumber).sum();
    }

    public Map<String, Integer> countTokens() {
        Map<String, Integer> textNumber = new HashMap<>();
        for (Text.Paragraph paragraph : this.getParagraphs()) {
            for (Text.Sentence sentence : paragraph.getSentences()) {
                sentence.countTokens(textNumber, sentence);
            }
        }
        return textNumber;
    }

    public int getCharacterNumber() {
        return characterNumber;
    }

    public List<Paragraph> getParagraphs() {
        return Collections.unmodifiableList(paragraphs);
    }

    public static class Paragraph {
        private final List<Sentence> sentences;
        private final int characterNumber;

        public Paragraph(List<Sentence> sentences) {
            this.sentences = sentences;
            characterNumber = sentences.stream().mapToInt(Sentence::getCharacterNumber).sum();
        }

        public int getCharacterNumber() {
            return characterNumber;
        }

        public List<Sentence> getSentences() {
            return Collections.unmodifiableList(sentences);
        }
    }

    public static class Sentence {
        private final List<String> tokens;
        private final String source;
        private final int characterNumber;

        public Sentence(List<String> tokens, String source) {
            this.tokens = tokens;
            characterNumber = tokens.stream().mapToInt(String::length).sum();
            this.source = source;
        }

        public int getCharacterNumber() {
            return characterNumber;
        }

        public String getSource() {
            return source;
        }

        public List<String> getTokens() {
            return Collections.unmodifiableList(tokens);
        }


        private void countTokens(Map<String, Integer> textNumber, Text.Sentence sentence) {
            for (String word : sentence.getTokens()) {
                textNumber.computeIfPresent(word, (k, v) -> v + 1);
                textNumber.putIfAbsent(word, 1);
            }
        }

        public Map<String, Integer> countTokens() {
            Map<String, Integer> textNumber = new HashMap<>();
            countTokens(textNumber, this);
            return textNumber;
        }

    }

}
