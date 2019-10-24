package com.oSsEtXiYs.rest;

import com.oSsEtXiYs.service.engine.EssayService;
import com.oSsEtXiYs.service.engine.TextParser;
import com.oSsEtXiYs.service.model.Essay;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/essay")
public class EssayController {

    private final TextParser textParser;
    private final EssayService classicEssayService;

    public EssayController(TextParser textParser, EssayService classicEssayService) {
        this.textParser = textParser;
        this.classicEssayService = classicEssayService;
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/classic",
            consumes = {MediaType.APPLICATION_JSON_VALUE}
    )
    public String classicEssay() {
        Essay essay = classicEssayService.pressText(textParser.parse(""));
        return "";
    }

}
