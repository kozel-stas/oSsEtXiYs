package com.oSsEtXiYs.service.model;

import java.util.List;

public class KeyEssay implements Essay {

    private final List<String> essay;

    public KeyEssay(List<String> essay) {
        this.essay = essay;
    }

    @Override
    public String getEssayAsString() {
        return String.join("\n", essay);
    }

    public List<String> getKeywords() {
        return essay;
    }

}
