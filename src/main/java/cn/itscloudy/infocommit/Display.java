package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.util.SwingUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.*;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Display {
    private static final String PATTERN_HEAD = " " + IcConst.get("pattern") + " ";
    public static final Color ACCENT_COLOR = SwingUtil.decodeColor("#CA8265", "#4E94DA");
    public static final Color BORDER_COLOR = SwingUtil.decodeColor("#F8A662", "#6391F9");

    @Getter
    private JPanel root;
    private JPanel displayPane;
    private JPanel amendModeNotice;
    private JLabel amendModeNoticeLabel;
    private final JLabel patternLabel;
    private final JLabel patternHead;

    private final StepCacheResolver stepCacheResolver;
    private final Pattern pattern;
    private final ComponentPopupBuilder prefixConfigPopupBuilder;
    @Getter
    private boolean amendMode = false;

    public Display(@NotNull Project project, @NotNull String basePath) {
        this.stepCacheResolver = new StepCacheResolver(basePath);
        this.pattern = new Pattern(project, stepCacheResolver);
        Form form = new Form(pattern);

        root.setBorder(new RoundCornerBorder(6, 1, BORDER_COLOR));
        JPanel configPane = form.getRoot();
        prefixConfigPopupBuilder = JBPopupFactory.getInstance().createComponentPopupBuilder(configPane, configPane)
                .setResizable(true)
                .setRequestFocus(true)
                .setCancelOnClickOutside(true)
                .setCancelOnWindowDeactivation(true);

        displayPane.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));

        patternHead = new JLabel();
        patternHead.setText(PATTERN_HEAD);
        patternHead.setOpaque(true);
        patternHead.setBackground(ACCENT_COLOR);

        patternLabel = new JLabel() {
            @Override
            public void setForeground(Color fg) {
                setText(pattern.toDisplayString());
            }
        };
        updateLabel();

        displayPane.add(patternHead);
        displayPane.add(patternLabel);

        amendModeNotice.setVisible(false);
        makeFormStarter(displayPane);
        makeFormStarter(patternHead);
        makeFormStarter(patternLabel);

        amendModeNoticeLabel.setText(IcConst.get("amendModeNotice"));
    }

    private void makeFormStarter(JComponent component) {
        component.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        FormStartListener listener = new FormStartListener();
        component.addMouseListener(listener);
    }

    public void updateLabel() {
        patternLabel.setText(pattern.toDisplayString());
    }

    public void updateCache() {
        updateLabel();
        stepCacheResolver.update(pattern);
        pattern.acceptAll();
    }

    public String instrumentMessage(String message) {
        return pattern.instrument(message);
    }

    public void setAmendMode(boolean amendMode) {
        this.amendMode = amendMode;
        amendModeNotice.setVisible(amendMode);
        displayPane.setVisible(!amendMode);
    }

    private class FormStartListener extends MouseAdapter {
        private long hideAt = 0;
        private final JBPopupListener popupListener;

        FormStartListener() {
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
