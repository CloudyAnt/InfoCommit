package cn.itscloudy.infocommit.context;

import cn.itscloudy.infocommit.util.CmdUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class IcStartupActivity implements StartupActivity {
    static final List<Consumer<IcProjectContext>> POST_PROJECT_STARTUP_TASKS = new ArrayList<>();
    private boolean firstTime = true;

    @Override
    public void runActivity(@NotNull Project project) {
        if (firstTime) {
            firstTime = false;
            init();
        }
        String basePath = project.getBasePath();
        if (basePath == null) {
            // no handling for project without base path
            return;
        }

        CmdUtil.Result insideWorkTreeResult = CmdUtil
                .runGitCommand(basePath, "rev-parse", "--is-inside-work-tree");
        if (!"true".equals(insideWorkTreeResult.getOutput())) {
            // no handling for project not in git work tree
            return;
        }

        IcProjectContextManager.openProject(project);
        POST_PROJECT_STARTUP_TASKS.forEach(task -> task.accept(IcProjectContextManager.getInstance(project)));
    }


    private void init() {
        // TODO add post project startup tasks
    }
}
