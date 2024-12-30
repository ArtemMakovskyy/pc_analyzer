package com.example.parser.model.hotline;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
@SQLDelete(sql = "UPDATE wines SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class GpuHotLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String totalName;
    private String name;
    private String memoryType;
    private String memorySize;
    private String shina;
    private String port;
    private String year;
    private String price;
    private Double avgBenchUserBenchmark;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
