package com.example.parser.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class Test {
   private final HtmlDocumentFetcher htmlDocumentFetcher;

}
