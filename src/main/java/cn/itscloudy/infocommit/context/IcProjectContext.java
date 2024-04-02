package cn.itscloudy.infocommit.context;

import cn.itscloudy.infocommit.Display;
import com.intellij.openapi.project.Project;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Getter
public class IcProjectContext {
    boolean loaded;
    private final boolean valid;
    private final Project project;
    private final String basePath;
    private Display display; // should be null if invalid
    @Setter
    private boolean amendMode;

    IcProjectContext(@NotNull Project project) {
        this.project = project;
        this.basePath = project.getBasePath();
        loaded = false;
        valid = basePath != null;
    }

    /**
     * Get the prefix display
     *
     * @return null if invalid
     */
    @Nullable
    public Display getDisplay() {
        if (!valid) {
            return null;
        }
        return synGetPrefixDisplay();
    }

    private synchronized Display synGetPrefixDisplay() {
        if (display == null) {
            display = new Display(project, basePath);
        }
        return display;
    }
}
