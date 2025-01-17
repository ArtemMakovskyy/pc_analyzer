package com.example.parser.service.userbenchmark;


import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.benchmark.user.UserBenchmarkGpuPageParser;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class GpuUserBenchmarkService {
    private final GpuUserBenchmarkRepository userBenchmarkGpuRepository;
    private final UserBenchmarkGpuPageParser userBenchmarkGpuPageParser;

//    @PostConstruct
    public void init(){
//        parseAllAndSaveToDbCopy();
    }

    public List<UserBenchmarkGpu> parseAllAndSaveToDb() {
        final List<UserBenchmarkGpu> gpuUserBenchmarkList
                = userBenchmarkGpuPageParser.parse(1);

        final List<UserBenchmarkGpu> gpuUserBenchmarks
                = userBenchmarkGpuRepository.saveAll(gpuUserBenchmarkList);

        return gpuUserBenchmarks;
    }

    public List<UserBenchmarkGpu> parseAllAndSaveToDbCopy() {
        //todo check if positions exists

        List<UserBenchmarkGpu> newGpuUserBenchmarkList
                = userBenchmarkGpuPageParser.parse(1);

        List<UserBenchmarkGpu> oldGpuUserBenchmarkList
                = userBenchmarkGpuRepository.findAll();

        final List<UserBenchmarkGpu> newItems
                = filterNewItems(oldGpuUserBenchmarkList, newGpuUserBenchmarkList);

        newItems.forEach(System.out::println);

//        final List<UserBenchmarkGpu> gpuUserBenchmarks
//                = userBenchmarkGpuRepository.saveAll(gpuUserBenchmarkList);

        return Collections.emptyList();
    }

    private static List<UserBenchmarkGpu> filterNewItems(List<UserBenchmarkGpu> oldList, List<UserBenchmarkGpu> newList) {

        Set<String> oldNames = oldList.stream()
                .map(UserBenchmarkGpu::getModel)
                .collect(Collectors.toSet());


        return newList.stream()
                .filter(newItem -> !oldNames.contains(newItem.getModel()))
                .collect(Collectors.toList());
    }
}
