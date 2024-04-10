package cn.itscloudy.infocommit.config;

import cn.itscloudy.infocommit.*;
import cn.itscloudy.infocommit.util.FilledLayeredPane;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class ProjectConfig implements SearchableConfigurable {
    private JPanel root;
    private JLayeredPane canvas;
    private JLabel introduction;
    private final ProjectConfigCanvasLower canvasLower;
    private final ProjectConfigCanvasHigher canvasHigher;

    ProjectConfig(@NotNull Project project) {
        IcProjectContext context = IcProjectContextManager.getInstance(project);
        introduction.setFont(new Font(null, Font.PLAIN, 16));
        MessagePattern messagePattern = context.getMessagePattern();

        FilledLayeredPane canvas = (FilledLayeredPane) this.canvas;
        canvasLower = new ProjectConfigCanvasLower(this, messagePattern);
        canvasHigher = new ProjectConfigCanvasHigher(this);

        canvasLower.higher = canvasHigher;
        canvasHigher.lower = canvasLower;

        JPanel lowerLayer = canvas.getLayerPanel(1);
        lowerLayer.setLayout(IcUI.FULL_LAYOUT);
        lowerLayer.add(canvasLower.getRoot());

        JPanel higherLayer = canvas.getLayerPanel(2);
        higherLayer.setLayout(new GridLayout(1, 1));
        higherLayer.add(canvasHigher.getRoot());
    }

    @Override
    public String getDisplayName() {
        return IcConst.NAME;
    }

    @Override
    public @Nullable JComponent createComponent() {
        return root;
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public void apply() throws ConfigurationException {

    }

    @Override
    public @NotNull @NonNls String getId() {
        return IcConst.ID;
    }

    private void createUIComponents() {
        this.canvas = new FilledLayeredPane(2);
    }
}
