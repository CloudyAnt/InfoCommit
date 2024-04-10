package cn.itscloudy.infocommit;

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

        IcProjectContext projectContext = IcProjectContextManager.getInstance(project);
        Display display = projectContext.getDisplay();
        if (display == null) {
            return;
        }

        String place = e.getPlace();
        if (place.equals("CommitMessage")) {
            // commit using dialog
            updateDialog(e, display);
        } else if (place.equals("ChangesView.CommitButtonsToolbar") || place.equals("ChangesView.CommitToolbar")) {
            // commit using tool window
            updateToolWindow(e, projectContext, display);
        }
    }

    private void updateDialog(@NotNull AnActionEvent e,
                              @NotNull Display display) {
        Object messageControl = e.getDataContext().getData("COMMIT_MESSAGE_CONTROL");
        if (messageControl != lastUpdatedData && messageControl instanceof JComponent) {
            lastUpdatedData = messageControl;
            ApplicationManager.getApplication().invokeLater(() -> {
                JComponent holderComp = (JComponent) ((JComponent) messageControl).getComponents()[1];
                holderComp.add(display.getRoot(), BorderLayout.SOUTH);
            });

        }
    }

    private void updateToolWindow(@NotNull AnActionEvent e,
                                  @NotNull IcProjectContext projectContext,
                                  @NotNull Display display) {
        Object panel = e.getDataContext().getData("Panel");
        if (panel instanceof CommitProjectPanelAdapter adapter) {
            String commitActionName = adapter.getCommitActionName();
            boolean amendMode = commitActionName.contains("Amend") || commitActionName.contains("amend");
            projectContext.setAmendMode(amendMode);
            if (panel != lastUpdatedData) {
                lastUpdatedData = panel;
                ApplicationManager.getApplication().invokeLater(() -> {
                    JComponent adapterComp = adapter.getComponent();
                    if (adapterComp == null) {
                        return;
                    }
                    adapterComp.add(display.getRoot(), BorderLayout.NORTH);
                });
            }
            display.setAmendMode(amendMode);
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }
}
