package com.example.parser.model.hotline;

import com.example.parser.model.user.benchmark.UserBenchmarkCpu;
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
@Table(name = "cpus_hotline")
@ToString
public class CpuHotLine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String url;
    private String prices;
    private Double avgPrice;

    private String socketType;
    private String frequency;
    private String l3Cache;
    private String cores;
    private String threads;
    private String packageType;
    private String releaseDate;
    private Integer propositionsQuantity;

    private String brand;
    private String type;
    private double baseFrequency;
    private double maxFrequency;
    private String coreName;
    private int coreCount;
    private int threadCount;

    private Double avgBenchUserBenchmark;

    @ManyToOne
    private UserBenchmarkCpu userBenchmarkCpu;

//    @Column(name = "is_deleted", nullable = false)
//    private boolean isDeleted = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CpuHotLine that = (CpuHotLine) o;
        return Double.compare(baseFrequency, that.baseFrequency) == 0 && Double.compare(maxFrequency, that.maxFrequency) == 0 && coreCount == that.coreCount && threadCount == that.threadCount && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(url, that.url) && Objects.equals(prices, that.prices) && Objects.equals(socketType, that.socketType) && Objects.equals(frequency, that.frequency) && Objects.equals(l3Cache, that.l3Cache) && Objects.equals(cores, that.cores) && Objects.equals(threads, that.threads) && Objects.equals(packageType, that.packageType) && Objects.equals(releaseDate, that.releaseDate) && Objects.equals(brand, that.brand) && Objects.equals(type, that.type) && Objects.equals(coreName, that.coreName) && Objects.equals(avgBenchUserBenchmark, that.avgBenchUserBenchmark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, prices, socketType, frequency, l3Cache, cores, threads, packageType, releaseDate, brand, type, baseFrequency, maxFrequency, coreName, coreCount, threadCount, avgBenchUserBenchmark);
    }

    //todo add try
}
