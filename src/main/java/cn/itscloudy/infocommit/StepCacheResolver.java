package cn.itscloudy.infocommit;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class StepCacheResolver {
    private final Map<String, String> cache = new HashMap<>();
    private final Path path;

    @SneakyThrows
    public StepCacheResolver(@NotNull String basePath) {
        this.path = Path.of(basePath, ".git", IcConst.STEP_CACHE_FILE_NAME);
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

    public void update(MessagePattern messagePattern) {
        messagePattern.iterateOverSteps(step -> cache.put(step.getKey(), step.getSegmentValue()));
        writeIntoFile();
    }

    private void writeIntoFile() {
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
