package com.example.parser.repository;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.hotline.GpuHotLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface GpuHotLineRepository extends JpaRepository<GpuHotLine,Long> {
    @Query("SELECT g FROM GpuHotLine g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<GpuHotLine> findByPartialNameIgnoreCase(@Param("name") String name);

    @Query("SELECT g FROM GpuHotLine g WHERE LOWER(g.name) LIKE LOWER(CONCAT('%', :name, '%')) AND g.userBenchmarkGpu IS NULL")
    List<GpuHotLine> findByPartialNameIgnoreCaseAndUserBenchmarkGpuIsNull(@Param("name") String name);

}
