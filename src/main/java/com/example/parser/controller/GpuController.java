package com.example.parser.controller;

import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class GpuController {

    @Autowired
    private GpuUserBenchmarkRepository gpuUserBenchmarkRepository;

    @PostConstruct
    public List<UserBenchmarkGpu> getAllWherePowerRequirementIsNull() {
        return gpuUserBenchmarkRepository.findAllByPowerRequirementIsNull();
    }

    @GetMapping("/gpus")
    public String listGpus(Model model) {
        List<UserBenchmarkGpu> gpus = getAllWherePowerRequirementIsNull();
        model.addAttribute("gpus", gpus);
        return "gpus";
    }

    @PostMapping("/saveGpus")
    public String saveGpus(@RequestParam("gpuId") List<Long> gpuIds,
                           @RequestParam("powerRequirement") List<Integer> powerRequirements) {
        for (int i = 0; i < gpuIds.size(); i++) {
            UserBenchmarkGpu gpu = gpuUserBenchmarkRepository.findById(gpuIds.get(i)).orElse(null);
            if (gpu != null) {
                gpu.setPowerRequirement(powerRequirements.get(i));
                gpuUserBenchmarkRepository.save(gpu);
            }
        }
        return "redirect:/gpus";
    }
}
