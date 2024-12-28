package com.example.parser.controller;

import com.example.parser.service.pda.PdaJsoupParser;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final PdaJsoupParser parser;

    @SneakyThrows
    @GetMapping
    public String test()  {
        parser.parse();
        return "test";
    }
}
