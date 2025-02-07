package com.example.parser.repository;

import com.example.parser.model.hotline.SsdHotLine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SsdHotLineRepository extends JpaRepository<SsdHotLine, Long> {
    @Query(value = """
<<<<<<< HEAD
            SELECT * 
            FROM parser.ssd_hotline
            WHERE avg_price IS NOT NULL AND propositions_quantity > :minPropositionQuantity 
            ORDER BY avg_price
            LIMIT 1
               """, nativeQuery = true)
=======
                SELECT * 
                FROM parser.ssd_hotline
                WHERE avg_price IS NOT NULL AND propositions_quantity > :minPropositionQuantity 
                ORDER BY avg_price
                LIMIT 1
            """, nativeQuery = true)
>>>>>>> develop
    SsdHotLine findTopByAvgPrice(@Param("minPropositionQuantity") int minPropositionQuantity);
}
