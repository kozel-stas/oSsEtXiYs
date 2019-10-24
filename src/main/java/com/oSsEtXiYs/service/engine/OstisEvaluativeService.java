package com.oSsEtXiYs.service.engine;

import com.oSsEtXiYs.service.model.Text;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.List;

@Service
public class OstisEvaluativeService implements ExternalEvaluativeService {

    private static final String API_PATH = "/api/idtf/find?substr=";

    private final List<String> ostisSystems;
    private final RestTemplate restTemplate;

    public OstisEvaluativeService(@Value("${ostis.hosts}") List<String> ostisSystems, RestTemplate restTemplate) {
        this.ostisSystems = ostisSystems;
        this.restTemplate = restTemplate;
    }

    @Override
    public Map<String, Double> evaluateText(Text text) {

        return null;
    }

    private JSONObject requestInfoFromOstisServise(String ostis, String text) {
        return new JSONObject(restTemplate.getForObject(ostis + API_PATH, String.class, text));
    }
}
