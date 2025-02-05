package com.example.parser.controller;

import com.example.parser.service.CreatorPc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/operations")
@RequiredArgsConstructor
public class PcOperationsController {

    private final CreatorPc creatorPc;

    @GetMapping("/")
    public String showIndexPage() {
        return "index";
    }

    @GetMapping
    public String showOperationsPage() {
        return "operations";
    }

    @PostMapping("/execute")
    @ResponseBody // Возвращаем JSON, а не редирект
    public Map<String, Object> executeOperation(@RequestParam(name = "updateUserBenchmarkCpu", required = false) boolean updateUserBenchmarkCpu,
                                                @RequestParam(name = "updateUserBenchmarkGpu", required = false) boolean updateUserBenchmarkGpu,
                                                @RequestParam(name = "updateHotline", required = false) boolean updateHotline,
                                                @RequestParam(name = "createPcList", required = false) boolean createPcList,
                                                @RequestParam(name = "saveReportToExcel", required = false) boolean saveReportToExcel) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean result = creatorPc.updateDataAndCreatePcList(
                    updateUserBenchmarkCpu,
                    updateUserBenchmarkGpu,
                    updateHotline,
                    createPcList,
                    saveReportToExcel);

            response.put("message", result ? "Операция выполнена успешно!" : "Ошибка выполнения операции!");
            response.put("success", result);
        } catch (Exception e) {
            response.put("message", "Произошла ошибка: " + e.getMessage());
            response.put("success", false);
        }
        return response; // Отправляем JSON-ответ
    }
}
