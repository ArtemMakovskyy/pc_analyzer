package com.example.parser.controller;

import com.example.parser.service.hotline.HotlineUpdaterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/hotline-updater")
@RequiredArgsConstructor
public class HotlineUpdaterController {

    private final HotlineUpdaterService hotlineUpdaterService;

    /**
     * Initiates parsing and updates for both CPU and GPU using multithreading.
     *
     * @return ResponseEntity indicating the status of the operation.
     */
//    @PostMapping("/parse-and-update")
//    public ResponseEntity<String> parseAllMT() {
//        hotlineUpdaterService.parseAllMT();
//        return ResponseEntity.ok("Parsing and updating of CPU and GPU data completed successfully.");
//    }
}