package com.example.parser.repository;

import com.example.parser.model.hotline.MemoryHotLine;
import com.example.parser.model.hotline.MotherBoardHotLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryHotLineRepository extends JpaRepository<MemoryHotLine,Long> {
}
