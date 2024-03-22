package cn.itscloudy.infocommit;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.*;
import com.intellij.ui.JBColor;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PrefixDisplay {
    private static final String PREFIX_HEAD = " " + IcConst.get("prefix") + " ";
    private static final Color HEAD_COLOR = new JBColor(JBColor.decode("#CA8265"), JBColor.decode("#4E94DA"));
    private static final Color BORDER_COLOR = new JBColor(JBColor.decode("#F8A662"), JBColor.decode("#6391F9"));

    @Getter
    private JPanel root;
    private JPanel displayPane;
    private final JLabel prefixLabel;

    private final StepCacheResolver stepCacheResolver;
    private final DefPrefixPattern messagePattern;
    private final PrefixConfig prefixConfig;
    private final ComponentPopupBuilder prefixConfigPopupBuilder;

    public PrefixDisplay(@NotNull Project project, @NotNull String basePath) {
        this.stepCacheResolver = new StepCacheResolver(basePath);
        this.messagePattern = new DefPrefixPattern(project, stepCacheResolver);
        this.prefixConfig = new PrefixConfig(messagePattern);
        JPanel configPane = prefixConfig.getRoot();
        prefixConfigPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(configPane, configPane)
                .setResizable(true)
                .setRequestFocus(true)
                .setCancelOnClickOutside(true)
                .setCancelOnWindowDeactivation(true);
        prefixConfigPopupBuilder.addListener(new JBPopupListener() {
            @Override
            public void onClosed(@NotNull LightweightWindowEvent event) {
                updateLabelAndCache();
            }
        });


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
        displayPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                showPrefixConfigDialog();
            }
        });
    }

    private void showPrefixConfigDialog() {
        JBPopup popup = prefixConfigPopupBuilder.createPopup();
        popup.showUnderneathOf(displayPane);
    }

    public void updateLabelAndCache() {
        prefixLabel.setText(getPrefix());
        stepCacheResolver.update(messagePattern.getSteps());
    }

    public String getPrefix() {
        return prefixConfig.getPrefix();
    }
}