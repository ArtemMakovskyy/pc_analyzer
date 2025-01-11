package com.example.parser.model.user.benchmark;

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
@Table(name = "cpus_user_benchmark")
@ToString
@SQLDelete(sql = "UPDATE wines SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
public class UserBenchmarkCpu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String model;
    private String manufacturer;
    /**
     * User Rating % – рейтинг пользователей в процентах.
     * Он основан на отзывах пользователей, которые тестировали
     * процессоры. Чем выше процент, тем больше положительных отзывов.
     */
    private Double userRating;
    /**
     * Value % – соотношение цена/производительность в процентах. Это оценка,
     * насколько выгоден процессор в своей ценовой категории.
     */
    private Double valuePercents;
    /**
     * Avg. Bench % – средний балл производительности процессора, полученный
     * из тестов. Это суммарная оценка, основанная на производительности в
     * различных сценариях, таких как однопоточная и многопоточная нагрузка.
     */
    private Double avgBench;
    /**
     * Memory Pts – производительность работы процессора с оперативной памятью
     * (в баллах). Это показывает, насколько эффективно процессор взаимодействует с памятью.
     */
    private Double memoryPercents;
    private Double price;
    private String urlOfCpu;

    //inner page
    private String partNumber;
    private String cpuSpecification;
    private Double desktopScore;
    private Double gamingScore;
    private Double workstationScore;
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;
}
