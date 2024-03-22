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
    private static final Color HEAD_COLOR = decodeColor("#CA8265", "#4E94DA");
    private static final Color BORDER_COLOR = decodeColor("#F8A662", "#6391F9");

    @Getter
    private JPanel root;
    private JPanel displayPane;
    private JPanel amendModeNotice;
    private final JLabel prefixLabel;
    private final JLabel prefixHead;

    private final StepCacheResolver stepCacheResolver;
    private final DefPrefixPattern messagePattern;
    private final PrefixConfig prefixConfig;
    private final ComponentPopupBuilder prefixConfigPopupBuilder;

    public PrefixDisplay(@NotNull Project project, @NotNull String basePath) {
        this.stepCacheResolver = new StepCacheResolver(basePath);
        this.messagePattern = new DefPrefixPattern(project, stepCacheResolver);
        this.prefixConfig = new PrefixConfig(messagePattern);

        root.setBorder(new RoundCornerBorder(6, 1, BORDER_COLOR));
        JPanel configPane = prefixConfig.getRoot();
        prefixConfigPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(configPane, configPane)
                .setResizable(true)
                .setRequestFocus(true)
                .setCancelOnClickOutside(true)
                .setCancelOnWindowDeactivation(true);

        displayPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        prefixHead = new JLabel();
        prefixHead.setText(PREFIX_HEAD);
        prefixHead.setOpaque(true);
        prefixHead.setBackground(HEAD_COLOR);

        prefixLabel = new JLabel();
        updateLabel();

        displayPane.add(prefixHead);
        displayPane.add(prefixLabel);

        amendModeNotice.setVisible(false);
        makePrefixConfigStarter(displayPane);
        makePrefixConfigStarter(prefixHead);
        makePrefixConfigStarter(prefixLabel);
    }

    private void makePrefixConfigStarter(JComponent component) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        PrefixConfigStartListener listener = new PrefixConfigStartListener();
        component.addMouseListener(listener);
    }

    public void updateLabel() {
        prefixLabel.setText(getPrefix());
    }

    public void updateCache() {
        updateLabel();
        stepCacheResolver.update(messagePattern.getSteps());
        messagePattern.acceptAll();
    }

    public String getPrefix() {
        return prefixConfig.getPrefix();
    }

    public void setAmendMode(boolean amendMode) {
        amendModeNotice.setVisible(amendMode);
        displayPane.setVisible(!amendMode);
    }

    private static JBColor decodeColor(String nm, String nmDark) {
        return new JBColor(JBColor.decode(nm), JBColor.decode(nmDark));
    }

    private class PrefixConfigStartListener extends MouseAdapter {
        private long hideAt = 0;
        private final JBPopupListener popupListener;

        PrefixConfigStartListener() {
            setAmendMode(false);

            popupListener = new JBPopupListener() {
                @Override
                public void onClosed(@NotNull LightweightWindowEvent event) {
                    hideAt = System.currentTimeMillis();
                    updateLabel();
                }
            };
        }

        @Override
        public synchronized void mousePressed(MouseEvent e) {
            if (System.currentTimeMillis() - hideAt < 100) {
                return;
            }
            JBPopup popup = prefixConfigPopupBuilder.createPopup();
            popup.addListener(popupListener);
            popup.showUnderneathOf(displayPane);
        }
    }
}
