package com.example.parser.service.userbenchmark;


import com.example.parser.model.user.benchmark.UserBenchmarkGpu;
import com.example.parser.repository.GpuUserBenchmarkRepository;
import com.example.parser.service.parse.benchmark.user.UserBenchmarkGpuPageParser;
import jakarta.annotation.PostConstruct;
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
    private static final int PARSE_ALL_PAGES_INDEX = -1;
    private final GpuUserBenchmarkRepository userBenchmarkGpuRepository;
    private final UserBenchmarkGpuPageParser userBenchmarkGpuPageParser;

    public List<UserBenchmarkGpu> parseAllAndSaveToDb() {

        List<UserBenchmarkGpu> newGpuUserBenchmarkList
                = userBenchmarkGpuPageParser.loadAndParse(
                true,
                PARSE_ALL_PAGES_INDEX);

        List<UserBenchmarkGpu> oldGpuUserBenchmarkList
                = userBenchmarkGpuRepository.findAll();

         List<UserBenchmarkGpu> newItems
                = filterNewItems(oldGpuUserBenchmarkList, newGpuUserBenchmarkList);

        return userBenchmarkGpuRepository.saveAll(newItems);
    }

    private static List<UserBenchmarkGpu> filterNewItems(
            List<UserBenchmarkGpu> oldList, List<UserBenchmarkGpu> newList) {

        Set<String> oldNames = oldList.stream()
                .map(UserBenchmarkGpu::getModel)
                .collect(Collectors.toSet());

        return newList.stream()
                .filter(newItem -> !oldNames.contains(newItem.getModel()))
                .collect(Collectors.toList());
    }
}
