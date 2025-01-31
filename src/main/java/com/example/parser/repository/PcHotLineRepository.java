package com.example.parser.repository;

import com.example.parser.model.Pc;
import com.example.parser.model.PcMarker;
import com.example.parser.model.hotline.CpuHotLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

public interface PcHotLineRepository extends JpaRepository<Pc, Long> {

    @Query("SELECT p FROM Pc p WHERE p.marker = :marker ORDER BY p.predictionGpuFpsFHD, p.price")
    List<Pc> findAllByMarkerOrderByPredictionPrice(@Param("marker") PcMarker marker);

    @Query("SELECT p FROM Pc p WHERE p.priceForFps != 0 ORDER BY p.priceForFps, p.predictionGpuFpsFHD, p.price")
    List<Pc> findPcsWithNonZeroPriceForFpsOrdered();

}
