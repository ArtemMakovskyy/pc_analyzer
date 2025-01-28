package com.example.parser.model;

import com.example.parser.model.hotline.CpuHotLine;
import com.example.parser.model.hotline.GpuHotLine;
import com.example.parser.model.hotline.MemoryHotLine;
import com.example.parser.model.hotline.MotherBoardHotLine;
import com.example.parser.model.hotline.PowerSupplierHotLine;
import com.example.parser.model.hotline.SsdHotLine;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@Table(name = "pc")
@ToString
public class Pc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private CpuHotLine cpu;
    @ManyToOne
    private MotherBoardHotLine motherboard;
    @ManyToOne
    private MemoryHotLine memory;
    @ManyToOne
    private GpuHotLine gpu;
    @ManyToOne
    private SsdHotLine ssd;
    @ManyToOne
    private PowerSupplierHotLine powerSupplier;

    private BigDecimal price;

}
