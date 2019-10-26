package com.oSsEtXiYs.rest;

import com.oSsEtXiYs.service.model.KeyEssay;
import org.springframework.stereotype.Service;

@Service
public class KeyEssayToScsConverter {

    String convertToScsContent(KeyEssay keyEssay) {
        return keyEssay.getEssayAsString();
    }

}
