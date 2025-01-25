package com.example.parser.repository;

import com.example.parser.model.hotline.MemoryHotLine;
import com.example.parser.model.hotline.SsdHotLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SsdHotLineRepository extends JpaRepository<SsdHotLine,Long> {
}
