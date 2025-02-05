package com.example.parser.controller;

import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/gpus")
@RequiredArgsConstructor
public class GpuController {

    private final GpuUserBenchmarkRepository gpuUserBenchmarkRepository;

    @GetMapping
    public String showGpusWithNullPowerRequirement(Model model) {
        List<UserBenchmarkGpu> gpus = gpuUserBenchmarkRepository.findAllByPowerRequirementIsNull();
        model.addAttribute("gpus", gpus);
        return "gpu-list";
    }

    @PostMapping("/update")
    public String updateGpuPowerRequirement(@RequestParam("id") Long id,
                                            @RequestParam("powerRequirement") Integer powerRequirement) {
        UserBenchmarkGpu gpu = gpuUserBenchmarkRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid GPU ID: " + id));
        gpu.setPowerRequirement(powerRequirement);
        gpuUserBenchmarkRepository.save(gpu);
        return "redirect:/gpus";
    }

}
