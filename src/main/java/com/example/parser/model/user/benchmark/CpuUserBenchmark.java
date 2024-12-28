package com.example.parser.model.user.benchmark;

import lombok.Data;

@Data
public class CpuUserBenchmark {
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
}
