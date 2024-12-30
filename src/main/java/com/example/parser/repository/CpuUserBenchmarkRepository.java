package com.example.parser.repository;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.user.benchmark.CpuUserBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CpuUserBenchmarkRepository extends JpaRepository<CpuUserBenchmark,Long> {
}
