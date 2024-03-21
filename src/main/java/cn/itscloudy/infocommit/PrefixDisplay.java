package cn.itscloudy.infocommit;

import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class PrefixDisplay {
    private static final String PREFIX_HEAD = " " + IcConst.get("prefix") + " ";
    private static final Color HEAD_COLOR = new JBColor(JBColor.decode("#CA8265"), JBColor.decode("#4E94DA"));
    private static final Color BORDER_COLOR = new JBColor(JBColor.decode("#F8A662"), JBColor.decode("#6391F9"));
    private final Project project;
    private final String basePath;

    @Getter
    private JPanel root;
    private JPanel configPane;
    private JPanel displayPane;
    private final JLabel prefixLabel;

    // created by createUIComponents
    private StepCacheResolver stepCacheResolver;
    private DefPrefixPattern messagePattern;
    private PrefixConfig prefixConfig;

    public PrefixDisplay(@NotNull Project project, @NotNull String basePath) {
        this.project = project;
        this.basePath = basePath;
        displayPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        displayPane.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        displayPane.setBorder(new RoundCornerBorder(6, 1, BORDER_COLOR));

        JLabel prefixHead = new JLabel();
        prefixHead.setText(PREFIX_HEAD);
        prefixHead.setOpaque(true);
        prefixHead.setBackground(HEAD_COLOR);

        prefixLabel = new JLabel();
        prefixLabel.setText(getPrefix());

        displayPane.add(prefixHead);
        displayPane.add(prefixLabel);
        configPane.setVisible(false);
    }

    public void togglePrefixConfigDialog() {
        if (configPane.isVisible()) {
            hidePrefixConfigDialog();
        } else{
            showPrefixConfigDialog();
        }
    }

    private void showPrefixConfigDialog() {
        configPane.setVisible(true);
        updateUI();
    }

    private void hidePrefixConfigDialog() {
        configPane.setVisible(false);
        updateUI();
        prefixLabel.setText(getPrefix());
    }

    private void updateUI() {
        root.setSize(root.getPreferredSize());
    }

    private void createUIComponents() {
        this.stepCacheResolver = new StepCacheResolver(basePath);
        this.messagePattern = new DefPrefixPattern(project, stepCacheResolver);
        this.prefixConfig = new PrefixConfig(messagePattern);
        configPane = prefixConfig.getRoot();
    }

    public void hidePrefixConfigAndUpdateCache() {
        hidePrefixConfigDialog();
        stepCacheResolver.update(messagePattern.getSteps());
    }

    public String getPrefix() {
        return prefixConfig.getPrefix();
    }
}
