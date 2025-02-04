package com.example.parser.controller;

import com.example.parser.service.CreatorPc;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String executeOperation(@RequestParam(name = "updateUserBenchmarkCpu", required = false) boolean updateUserBenchmarkCpu,
                                   @RequestParam(name = "updateUserBenchmarkGpu", required = false) boolean updateUserBenchmarkGpu,
                                   @RequestParam(name = "updateHotline", required = false) boolean updateHotline,
                                   @RequestParam(name = "createPcList", required = false) boolean createPcList,
                                   @RequestParam(name = "saveReportToExcel", required = false) boolean saveReportToExcel,
                                   RedirectAttributes redirectAttributes) {
        try {
            boolean result = creatorPc.updateDataAndCreatePcList(
                    updateUserBenchmarkCpu,
                    updateUserBenchmarkGpu,
                    updateHotline,
                    createPcList,
                    saveReportToExcel);
            redirectAttributes.addFlashAttribute("message", result ? "Операция выполнена успешно!" : "Ошибка выполнения операции!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Произошла ошибка: " + e.getMessage());
        }
        return "redirect:/operations";
    }
}
