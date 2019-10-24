package com.oSsEtXiYs.rest;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

@RestController
@RequestMapping("/parse")
public class ParsingController {

    private final Parser parser;

    public ParsingController(Parser parser) {
        this.parser = parser;
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/file"
    )
    public String fromFile(@RequestParam("file") MultipartFile file) throws IOException, TikaException, SAXException {
        return parseInputStream(file.getInputStream());
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/url"
    )
    public String fromUrl(@RequestParam("url") String uri) throws IOException, TikaException, SAXException {
        return parseInputStream(new URL(uri).openStream());
    }

    private String parseInputStream(InputStream inputStream) throws IOException, SAXException, TikaException {
        ContentHandler contentHandler = new BodyContentHandler();
        parser.parse(inputStream, contentHandler, new Metadata(), new ParseContext());
        return contentHandler.toString();
    }

}
