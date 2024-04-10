package cn.itscloudy.infocommit;

import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
public class IcProjectContext {
    boolean loaded;
    private final boolean valid;
    private final Project project;
    private final String basePath;

    // these should be null if invalid
    private Display display;
    private MessagePattern messagePattern;
    private StepCacheResolver stepCacheResolver;

    @Setter
    private boolean amendMode;

    private final Object GETTER_LOCK = new Object();

    IcProjectContext(@NotNull Project project) {
        this.project = project;
        this.basePath = project.getBasePath();
        loaded = false;
        valid = basePath != null;
    }

    public Display getDisplay() {
        if (!valid) {
            return null;
        }
        synchronized (GETTER_LOCK) {
            if (display == null) {
                display = new Display(this);
            }
            return display;
        }
    }

    public MessagePattern getMessagePattern() {
        if (!valid) {
            return null;
        }
        synchronized (GETTER_LOCK) {
            if (messagePattern == null) {
                messagePattern = new MessagePattern(project, getStepCacheResolver());
            }
            return messagePattern;
        }
    }

    public StepCacheResolver getStepCacheResolver() {
        if (!valid) {
            return null;
        }
        synchronized (GETTER_LOCK) {
            if (stepCacheResolver == null) {
                stepCacheResolver = new StepCacheResolver(basePath);
            }
            return stepCacheResolver;
        }
    }
}
