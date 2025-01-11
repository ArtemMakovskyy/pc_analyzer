package com.example.parser.repository;

import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CpuUserBenchmarkRepository extends JpaRepository<UserBenchmarkCpu,Long> {
}
