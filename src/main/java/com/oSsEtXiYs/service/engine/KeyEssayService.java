package com.oSsEtXiYs.service.engine;

import com.oSsEtXiYs.service.model.KeyEssay;
import com.oSsEtXiYs.service.model.Text;
import org.apache.commons.math3.stat.descriptive.rank.Percentile;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Service
public class KeyEssayService implements EssayService {

    private final ExternalEvaluativeService externalEvaluativeService;

    public KeyEssayService(ExternalEvaluativeService externalEvaluativeService) {
        this.externalEvaluativeService = externalEvaluativeService;
    }

    @Override
    public KeyEssay pressText(Text text) {
        Map<String, Integer> textNumber = text.countTokens();
        Map<String, Double> externalWeight = externalEvaluativeService.evaluateText(text);
        int frequentSum = textNumber.values().stream().mapToInt(i -> i).sum();
        Map<String, Double> tokenFrequent = new HashMap<>();
        for (Map.Entry<String, Integer> token : textNumber.entrySet()) {
            tokenFrequent.put(token.getKey(), ((double) token.getValue() / frequentSum) * externalWeight.getOrDefault(token.getKey(), 0D));
        }
        double median = new Percentile(75).evaluate(tokenFrequent.values().stream().mapToDouble(d -> d).toArray());
        tokenFrequent.values().removeIf(v -> v < median);
        return new KeyEssay(new ArrayList<>(tokenFrequent.keySet()));
    }

}
