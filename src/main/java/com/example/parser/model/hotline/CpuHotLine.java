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
@Table(name = "cpus_hotline")
@ToString
@SQLDelete(sql = "UPDATE wines SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class CpuHotLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String prices;

    private String socketType;
    private String frequency;
    private String l3Cache;
    private String cores;
    private String threads;
    private String packageType;
    private String releaseDate;


    private String brand;
    private String type;
    private double baseFrequency;
    private double maxFrequency;
    private String coreName;
    private int coreCount;
    private int threadCount;

    private Double avgBenchUserBenchmark;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
