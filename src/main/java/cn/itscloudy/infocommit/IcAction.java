package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.context.IcProjectContext;
import cn.itscloudy.infocommit.context.IcProjectContextManager;
import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.vcs.commit.CommitProjectPanelAdapter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class IcAction extends AnAction {

    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        // TODO open config dialog
    }

    Object lastUpdatedData;

    @Override
    public void update(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if (project == null) {
            return;
        }

        Object data = e.getDataContext().getData("Panel");
        if (data != lastUpdatedData && data instanceof CommitProjectPanelAdapter adapter) {
            lastUpdatedData = data;
            IcProjectContext projectContext = IcProjectContextManager.getInstance(project);
            final PrefixDisplay prefixDisplay = projectContext.getPrefixDisplay();
            if (prefixDisplay == null) {
                return;
            }

            ApplicationManager.getApplication().invokeLater(() -> {
                JComponent adapterComp = adapter.getComponent();
                Component originalComp;
                if (adapterComp == null || (originalComp = adapterComp.getComponents()[0]) == null) {
                    return;
                }
                adapterComp.removeAll();

                JPanel newComp = new JPanel();
                newComp.setLayout(new BorderLayout());

                newComp.add(prefixDisplay.getRoot(), BorderLayout.NORTH);
                newComp.add(originalComp, BorderLayout.CENTER);
                adapterComp.add(newComp);
            });
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }
}
