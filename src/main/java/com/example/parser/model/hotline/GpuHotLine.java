package com.example.parser.model.hotline;

import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Table(name = "gpus_hotline")
@ToString
public class GpuHotLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String manufacturer;
    private String memoryType;
    private String memorySize;
    private String shina;
    private String port;
    private String prices;
    private Double avgPrice;
    private Integer propositionsQuantity;
    @ManyToOne
    private UserBenchmarkGpu userBenchmarkGpu;

}
