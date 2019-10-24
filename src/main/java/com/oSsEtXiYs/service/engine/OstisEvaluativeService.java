package com.oSsEtXiYs.service.engine;

import com.oSsEtXiYs.service.model.Text;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class OstisEvaluativeService implements ExternalEvaluativeService {

    private static final String API_PATH = "/api/idtf/find/?substr={token}";

    private final List<String> ostisSystems;
    private final RestTemplate restTemplate;

    public OstisEvaluativeService(@Value("${ostis.hosts}") List<String> ostisSystems, RestTemplate restTemplate) {
        this.ostisSystems = ostisSystems;
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, Double> evaluateText(Text text) {
        Map<String, Integer> tokenUsage = new HashMap<>();
        for (Text.Paragraph paragraph : text.getParagraphs()) {
            for (Text.Sentence sentence : paragraph.getSentences()) {
                for (String token : sentence.getTokens()) {
                    for (String ostis : ostisSystems) {
                        tokenUsage.computeIfAbsent(token, key -> requestInfoFromOstisService(ostis, key) + 1);
                    }
                }
            }
        }
        double count = tokenUsage.values().stream().mapToInt(k -> k).count();
        Map<String, Double> result = new HashMap<>();
        for (Map.Entry<String, Integer> entry : tokenUsage.entrySet()) {
            result.put(entry.getKey(), entry.getValue() / count);
        }
        return result;
    }

    private int requestInfoFromOstisService(String ostis, String text) {
        return new JSONObject(Objects.requireNonNull(restTemplate.getForObject(ostis + API_PATH, String.class, text))).getJSONArray("main").length();
    }

}
