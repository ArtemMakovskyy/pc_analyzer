package com.example.parser.repository;

import com.example.parser.model.hotline.CpuHotLine;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CpuHotLineRepository extends JpaRepository<CpuHotLine, Long> {
    @Query("SELECT c FROM CpuHotLine c WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    List<CpuHotLine> findByPartialNameIgnoreCase(@Param("name") String name);


    /**
     SELECT
     c.*
     FROM
     cpus_hotline c
     JOIN (
     SELECT
     user_benchmark_cpu_id,
     MIN(avg_price) AS min_price
     FROM
     cpus_hotline
     WHERE
     avg_price > 0
     GROUP BY
     user_benchmark_cpu_id
     ) grouped ON c.user_benchmark_cpu_id = grouped.user_benchmark_cpu_id AND c.avg_price = grouped.min_price
     WHERE
     c.avg_price > 0  AND c.propositions_quantity
     ORDER BY
     c.avg_price;
     */
    @Query("SELECT c FROM CpuHotLine c " +
            "WHERE c.avgPrice > 0 AND c.propositionsQuantity > 5 AND " +
            "c.avgPrice = (SELECT MIN(c2.avgPrice) " +
            "             FROM CpuHotLine c2 " +
            "             WHERE c2.userBenchmarkCpu.id = c.userBenchmarkCpu.id AND c2.avgPrice > 0) " +
            "ORDER BY c.avgPrice")
    List<CpuHotLine> findMinPriceGroupedByUserBenchmarkCpuId();

}
