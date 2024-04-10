package cn.itscloudy.infocommit;

import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class IcProjectContextManager {

    private static final Map<Project, IcProjectContext> MAP = new HashMap<>();

    static void createForProject(@NotNull Project project) {
        MAP.entrySet().removeIf(entry -> entry.getKey().isDisposed());
        getInstance(project).loaded = true;
    }

    public static synchronized IcProjectContext getInstance(Project project) {
        return MAP.computeIfAbsent(project, k -> new IcProjectContext(project));
    }
}
