package com.oSsEtXiYs.rest;

import com.oSsEtXiYs.rest.model.EssayMethod;
import com.oSsEtXiYs.service.engine.EssayService;
import com.oSsEtXiYs.service.engine.TextParser;
import com.oSsEtXiYs.service.model.Essay;
import com.oSsEtXiYs.service.model.Text;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.Min;

@RestController
@RequestMapping("/essay")
@Validated
public class EssayController {

    private final TextParser textParser;
    private final EssayService classicEssayService;

    public EssayController(TextParser textParser, EssayService classicEssayService) {
        this.textParser = textParser;
        this.classicEssayService = classicEssayService;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/classic",
            produces = {MediaType.APPLICATION_JSON_VALUE}
    )
    public com.oSsEtXiYs.rest.model.Essay classicEssay(@Valid @Nonnull @Min(1) @RequestBody String sourceText) {
        return new com.oSsEtXiYs.rest.model.Essay(
                sourceText,
                constructResult(classicEssayService.pressText(textParser.parse(sourceText))),
                EssayMethod.CLASSIC
        );
    }

    private static String constructResult(Essay essay) {
        StringBuilder result = new StringBuilder();
        for (Text.Paragraph paragraph : essay.getParagraphs()) {
            for (Text.Sentence sentence : paragraph.getSentences()) {
                result.append(sentence.getSource()).append(" ");
            }
        }
        return result.toString();
    }

}
