package com.oSsEtXiYs.service.dao;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.Set;

@Repository
public class StopWordsRepository implements InitializingBean {

    private final MongoOperations mongoOperations;
    private volatile StopWords stopWords = new StopWords(Collections.emptySet());

    public StopWordsRepository(MongoOperations mongoOperations) {
        this.mongoOperations = mongoOperations;
    }

    @Override
    public void afterPropertiesSet() {
        stopWords = mongoOperations.query(StopWords.class).first().orElse(new StopWords(Collections.emptySet()));
    }

    public Set<String> getStopWords() {
        return Collections.unmodifiableSet(stopWords.getStopWords());
    }

    @Document(collection = "StopWords")
    private static class StopWords {

        @Field("stop_words")
        private final Set<String> stopWords;

        private StopWords(Set<String> stopWords) {
            this.stopWords = stopWords;
        }

        private Set<String> getStopWords() {
            return stopWords;
        }

    }

}
