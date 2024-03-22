package cn.itscloudy.infocommit;

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

        PrefixDisplay prefixDisplay = IcProjectContextManager.getInstance(project).getPrefixDisplay();
        if (prefixDisplay == null) {
            return;
        }

        String place = e.getPlace();
        if (place.equals("CommitMessage")) {
            // commit using dialog
            updateDialog(e, prefixDisplay);
        } else if (place.equals("ChangesView.CommitButtonsToolbar") || place.equals("ChangesView.CommitToolbar")) {
            // commit using tool window
            updateToolWindow(e, prefixDisplay);
        }
    }

    private void updateDialog(@NotNull AnActionEvent e,
                              @NotNull PrefixDisplay prefixDisplay) {
        Object messageControl = e.getDataContext().getData("COMMIT_MESSAGE_CONTROL");
        if (messageControl != lastUpdatedData && messageControl instanceof JComponent) {
            lastUpdatedData = messageControl;
            ApplicationManager.getApplication().invokeLater(() -> {
                JComponent holderComp = (JComponent) ((JComponent) messageControl).getComponents()[1];
                holderComp.add(prefixDisplay.getRoot(), BorderLayout.SOUTH);
            });

        }
    }

    private void updateToolWindow(@NotNull AnActionEvent e,
                                  @NotNull PrefixDisplay prefixDisplay) {
        Object panel = e.getDataContext().getData("Panel");
        if (panel != lastUpdatedData && panel instanceof CommitProjectPanelAdapter adapter) {
            lastUpdatedData = panel;
            ApplicationManager.getApplication().invokeLater(() -> {
                JComponent adapterComp = adapter.getComponent();
                if (adapterComp == null) {
                    return;
                }
                adapterComp.add(prefixDisplay.getRoot(), BorderLayout.NORTH);
            });
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }
}
