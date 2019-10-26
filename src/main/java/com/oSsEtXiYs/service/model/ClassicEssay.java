package com.oSsEtXiYs.service.model;

import java.util.List;

public class ClassicEssay extends Text implements Essay {

    public ClassicEssay(List<Text.Paragraph> paragraphs) {
        super(paragraphs);
    }

    @Override
    public String getEssayAsString() {
        StringBuilder result = new StringBuilder();
        for (Text.Paragraph paragraph : this.getParagraphs()) {
            for (Text.Sentence sentence : paragraph.getSentences()) {
                result.append(sentence.getSource()).append(" ");
            }
        }
        return result.toString();
    }
}
