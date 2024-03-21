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
                FilledLayeredPane filledLayeredPane = new FilledLayeredPane(2);
                JPanel bottomLayer = filledLayeredPane.getLayerPanel(1);
                bottomLayer.setLayout(new GridLayout(1, 1));
                bottomLayer.add(originalComp);

                JPanel topLayer = filledLayeredPane.getLayerPanel(2);
                topLayer.setLayout(null);
                JPanel floater = prefixDisplay.getRoot();
                Dimension preferredSize = floater.getPreferredSize();
                floater.setBounds(0, preferredSize.height + 10, preferredSize.width, preferredSize.height);
                topLayer.add(floater);

                adapterComp.add(filledLayeredPane);
                Draggable.make(floater, floater, originalComp, prefixDisplay::togglePrefixConfigDialog);
            });
        }
    }

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return super.getActionUpdateThread();
    }
}
