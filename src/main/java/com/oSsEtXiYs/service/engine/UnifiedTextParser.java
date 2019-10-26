package com.oSsEtXiYs.service.engine;

import com.oSsEtXiYs.service.dao.StopWordsRepository;
import com.oSsEtXiYs.service.model.Text;

import java.text.BreakIterator;
import java.util.List;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.regex.Pattern;

@Service
public class UnifiedTextParser implements TextParser {

    private static final String PARAGRAPH_SEPARATOR = "\t";
    private static final Pattern DIGIT_PATTERN = Pattern.compile("^\\d*$");

    private final StopWordsRepository stopWordsRepository;

    public UnifiedTextParser(StopWordsRepository stopWordsRepository) {
        this.stopWordsRepository = stopWordsRepository;
    }

    @Override
    public Text parse(String source) {
        String[] paragraphs = source.split(PARAGRAPH_SEPARATOR);
        List<Text.Paragraph> parsedParagraphs = new ArrayList<>();
        for (String paragraph : paragraphs) {
            List<Text.Sentence> sentences = new ArrayList<>();
            BreakIterator breakIterator = BreakIterator.getSentenceInstance();
            breakIterator.setText(paragraph);
            int wordBoundaryIndex = breakIterator.first();
            int prevIndex = 0;
            while (wordBoundaryIndex != BreakIterator.DONE) {
                String sentence = paragraph.substring(prevIndex, wordBoundaryIndex);
                if (isSentence(sentence)) {
                    sentences.add(parseSentence(sentence));
                }
                prevIndex = wordBoundaryIndex;
                wordBoundaryIndex = breakIterator.next();
            }
            parsedParagraphs.add(new Text.Paragraph(sentences));
        }
        return new Text(parsedParagraphs);
    }

    private Text.Sentence parseSentence(String sentence) {
        List<String> words = new ArrayList<>();
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(sentence);
        int wordBoundaryIndex = breakIterator.first();
        int prevIndex = 0;
        while (wordBoundaryIndex != BreakIterator.DONE) {
            String word = sentence.substring(prevIndex, wordBoundaryIndex);
            if (isWord(word)) {
                words.add(word.trim());
            }
            prevIndex = wordBoundaryIndex;
            wordBoundaryIndex = breakIterator.next();
        }
        return new Text.Sentence(words, sentence);
    }

    private boolean isWord(String word) {
        if (word.length() == 1) {
            return Character.isLetter(word.charAt(0));
        }
        return !"".equals(word.trim()) && !stopWordsRepository.getStopWords().contains(word.toLowerCase()) && !DIGIT_PATTERN.matcher(word).find();
    }

    private boolean isSentence(String sentence) {
        return !"".equals(sentence);
    }

}
