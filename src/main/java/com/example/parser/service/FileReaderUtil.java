package com.example.parser.service;

import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class FileReaderUtil {
    private final static String PATH = "C:\\Users\\Artem\\Documents\\Java\\UltimateJetBrains\\tutorials\\ms1\\parser\\parser\\src\\main\\resources\\static\\gpu_power_requirements.txt";

//    @PostConstruct
    public void readFile() {
        try {
            List<String> lines = Files.readAllLines(Path.of(PATH));
            lines.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
