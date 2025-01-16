package com.example.parser.repository;

import com.example.parser.model.hotline.CpuHotLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CpuHotLineRepository extends JpaRepository<CpuHotLine, Long> {
    @Query("SELECT c FROM CpuHotLine c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<CpuHotLine> findByPartialNameIgnoreCase(@Param("name") String name);
}
