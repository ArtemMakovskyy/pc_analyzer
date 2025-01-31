package com.example.parser.controller;

import com.example.parser.model.Pc;
import com.example.parser.service.hotline.CreatorPc;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pcs")
@RequiredArgsConstructor
public class PcController {

    private final CreatorPc creatorPc;

    @GetMapping
    public List<Pc> getAllPcs() {
        return creatorPc.getAll();
    }

    @PostMapping("/update")
    public boolean updateDataAndCreatePcList(
            @RequestParam boolean updateUserBenchmarkCpu,
            @RequestParam boolean updateUserBenchmarkGpu,
            @RequestParam boolean updateHotline,
            @RequestParam boolean createPcList) {
        creatorPc.updateDataAndCreatePcList(updateUserBenchmarkCpu, updateUserBenchmarkGpu, updateHotline, createPcList);
    return true;
    }

}