package com.example.parser.repository;

import com.example.parser.model.user.benchmark.GpuUserBenchmark;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBenchmarkGpuRepository extends JpaRepository<GpuUserBenchmark,Long> {
}
