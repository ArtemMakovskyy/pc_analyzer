package com.example.parser.repository;

import com.example.parser.model.Pc;
import com.example.parser.model.hotline.CpuHotLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface PcHotLineRepository extends JpaRepository<Pc, Long> {
    @Query("SELECT p FROM Pc p WHERE p.predictionGpuFpsFHD <> 0 ORDER BY p.predictionGpuFpsFHD ASC, p.price ASC")
    List<Pc> findAllByPredictionGpuFpsFHDNotZeroOrderByPredictionGpuFpsFHDAscPriceAsc();
}
