package com.oSsEtXiYs.service.model;

import java.util.Collections;
import java.util.List;

public class Text {

    private final List<Paragraph> paragraphs;
    private final int characterNumber;

    public Text(List<Paragraph> paragraphs) {
        this.paragraphs = paragraphs;
        this.characterNumber = paragraphs.stream().mapToInt(Paragraph::getCharacterNumber).sum();
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

        public List<String> getTokens() {
            return Collections.unmodifiableList(tokens);
        }

    }

}
