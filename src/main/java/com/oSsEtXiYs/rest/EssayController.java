package com.oSsEtXiYs.rest;

import com.oSsEtXiYs.rest.model.EssayMethod;
import com.oSsEtXiYs.service.engine.EssayService;
import com.oSsEtXiYs.service.engine.TextParser;
import com.oSsEtXiYs.service.model.KeyEssay;
import org.apache.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/essay")
public class EssayController {

    private final TextParser textParser;
    private final EssayService classicEssayService;
    private final EssayService keyEssayService;
    private final KeyEssayToScsConverter keyEssayToScsConverter;

    public EssayController(TextParser textParser, EssayService classicEssayService, EssayService keyEssayService, KeyEssayToScsConverter keyEssayToScsConverter) {
        this.textParser = textParser;
        this.classicEssayService = classicEssayService;
        this.keyEssayService = keyEssayService;
        this.keyEssayToScsConverter = keyEssayToScsConverter;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/classic",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public com.oSsEtXiYs.rest.model.Essay classicEssay(@RequestBody String sourceText) {
        return new com.oSsEtXiYs.rest.model.Essay(
                sourceText,
                classicEssayService.pressText(textParser.parse(sourceText)).getEssayAsString(),
                EssayMethod.CLASSIC
        );
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/key",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public com.oSsEtXiYs.rest.model.Essay keyEssay(@RequestBody String sourceText) {
        return new com.oSsEtXiYs.rest.model.Essay(
                sourceText,
                keyEssayService.pressText(textParser.parse(sourceText)).getEssayAsString(),
                EssayMethod.KEY
        );
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/key/scs"
    )
    public void keyEssayScs(@RequestBody String sourceText, HttpServletResponse response) throws IOException {
        byte[] content = keyEssayToScsConverter.convertToScsContent((KeyEssay) keyEssayService.pressText(textParser.parse(sourceText))).getBytes();
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Disposition", "inline; filename=" + UUID.randomUUID().toString() + ".scs");
        response.setHeader(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length));
        response.getOutputStream().write(content);
    }

}
