package cn.itscloudy.infocommit.config;

import cn.itscloudy.infocommit.IcConst;
import cn.itscloudy.infocommit.IcLayouts;
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
    private final Project project;
    private JPanel root;
    private JLayeredPane canvas;
    private JLabel introduction;

    ProjectConfig(@NotNull Project project) {
        this.project = project;
        introduction.setFont(new Font(null, Font.PLAIN, 16));
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
        FilledLayeredPane canvas = new FilledLayeredPane(2);
        this.canvas = canvas;
        ConfigCanvasHigherLayer canvasHigherLayer = new ConfigCanvasHigherLayer(this);
        ConfigCanvasLowerLayer canvasLowerLayer = new ConfigCanvasLowerLayer(this);
        JPanel lower = canvas.getLayerPanel(1);
        lower.setLayout(IcLayouts.FULL);
        lower.add(canvasLowerLayer.getRoot());

        JPanel higher = canvas.getLayerPanel(2);
        higher.setLayout(IcLayouts.FULL);
        higher.add(canvasHigherLayer.getRoot());

    }
}
