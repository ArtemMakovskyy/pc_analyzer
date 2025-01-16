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

    @ManyToOne
    private UserBenchmarkGpu userBenchmarkGpu;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GpuHotLine that = (GpuHotLine) o;
        return Objects.equals(id, that.id) && Objects.equals(totalName, that.totalName) && Objects.equals(name, that.name) && Objects.equals(memoryType, that.memoryType) && Objects.equals(memorySize, that.memorySize) && Objects.equals(shina, that.shina) && Objects.equals(port, that.port) && Objects.equals(year, that.year) && Objects.equals(price, that.price) && Objects.equals(avgBenchUserBenchmark, that.avgBenchUserBenchmark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, totalName, name, memoryType, memorySize, shina, port, year, price, avgBenchUserBenchmark);
    }
}
