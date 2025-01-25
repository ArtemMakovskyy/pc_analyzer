package com.example.parser.repository;

import com.example.parser.model.hotline.PowerSupplierHotLine;
import com.example.parser.model.hotline.SsdHotLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PowerSupplierHotLineRepository extends JpaRepository<PowerSupplierHotLine,Long> {
}
