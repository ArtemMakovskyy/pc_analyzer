package com.example.parser.repository;

import com.example.parser.model.user.benchmark.CpuUserBenchmark;
import com.example.parser.model.user.benchmark.GpuUserBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GpuUserBenchmarkRepository extends JpaRepository<GpuUserBenchmark,Long> {
}
