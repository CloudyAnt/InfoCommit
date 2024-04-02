package cn.itscloudy.infocommit.context;

import cn.itscloudy.infocommit.PrefixDisplay;
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
    private PrefixDisplay prefixDisplay; // should be null if invalid
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
    public PrefixDisplay getPrefixDisplay() {
        if (!valid) {
            return null;
        }
        return synGetPrefixDisplay();
    }

    private synchronized PrefixDisplay synGetPrefixDisplay() {
        if (prefixDisplay == null) {
            prefixDisplay = new PrefixDisplay(project, basePath);
        }
        return prefixDisplay;
    }
}
