package com.oSsEtXiYs.service.engine;

import com.oSsEtXiYs.service.model.Text;

import java.util.Map;

public interface ExternalEvaluativeService {

    Map<String, Double> evaluateText(Text text);

}
