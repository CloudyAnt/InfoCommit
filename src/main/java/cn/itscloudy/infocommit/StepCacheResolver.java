package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.step.CommitStep;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StepCacheResolver {
    private final Map<String, String> cache = new HashMap<>();
    private final Path path;

    @SneakyThrows
    public StepCacheResolver(@NotNull String basePath) {
        this.path = Path.of(basePath, ".git", "infoCommitSteps");
        File file = path.toFile();
        if (file.isFile()) {
            String cache = Files.readString(path);
            String[] split = cache.split("\n");
            for (String s : split) {
                String[] kv = s.split("=");
                this.cache.put(kv[0], kv[1]);
            }
        }
    }

    public void update(List<CommitStep> steps) {
        for (CommitStep step : steps) {
            cache.put(step.getKey(), step.getStepSegment().getSegmentValue());
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : cache.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("\n");
        }
        try {
            Files.writeString(path, sb.toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    public String get(String key) {
        return cache.get(key);
    }
}
