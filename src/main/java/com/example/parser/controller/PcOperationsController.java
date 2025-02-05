package com.example.parser.controller;

import com.example.parser.service.CreatorPcService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/operations")
@RequiredArgsConstructor
public class PcOperationsController {

    private final CreatorPcService creatorPcService;

    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping
    public String showOperationsPage() {
        return "operations";
    }

    @PostMapping("/execute")
    @ResponseBody
    public Map<String, Object> executeOperation(
            @RequestParam(name = "updateUserBenchmarkCpu", required = false)
            boolean updateUserBenchmarkCpu,
            @RequestParam(name = "updateUserBenchmarkGpu", required = false)
            boolean updateUserBenchmarkGpu,
            @RequestParam(name = "updateHotline", required = false)
            boolean updateHotline,
            @RequestParam(name = "createPcList", required = false)
            boolean createPcList,
            @RequestParam(name = "saveReportToExcel", required = false)
            boolean saveReportToExcel) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = creatorPcService.updateDataAndCreatePcList(
                    updateUserBenchmarkCpu,
                    updateUserBenchmarkGpu,
                    updateHotline,
                    createPcList,
                    saveReportToExcel);

            response.put("message", result ? "Операція виконана успішно!"
                    : "Помилка виконання операції!");
            response.put("success", result);
        } catch (Exception e) {
            response.put("message", "Виникла помилка: " + e.getMessage());
            response.put("success", false);
        }
        return response;
    }
}
