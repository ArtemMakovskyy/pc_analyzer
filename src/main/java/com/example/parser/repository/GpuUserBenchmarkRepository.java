package com.example.parser.repository;

import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GpuUserBenchmarkRepository extends JpaRepository<UserBenchmarkGpu,Long> {
}
