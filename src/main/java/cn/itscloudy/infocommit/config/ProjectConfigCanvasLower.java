package cn.itscloudy.infocommit.config;

import cn.itscloudy.infocommit.IcConst;
import cn.itscloudy.infocommit.IcUI;
import cn.itscloudy.infocommit.MessagePattern;
import cn.itscloudy.infocommit.MessagePatternSeg;
import cn.itscloudy.infocommit.ui.RoundCornerBorder;
import cn.itscloudy.infocommit.ui.RoundCornerLabel;
import cn.itscloudy.infocommit.util.IcStringUtil;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBEmptyBorder;
import com.intellij.util.ui.JBUI;
import lombok.Getter;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class ProjectConfigCanvasLower {
    private static final Font FONT = new Font(null, Font.PLAIN, 18);

    private final ProjectConfig projectConfig;
    private final MessagePattern messagePattern;
    @Getter
    private JPanel root;
    ProjectConfigCanvasHigher higher;

    public ProjectConfigCanvasLower(ProjectConfig projectConfig, MessagePattern messagePattern) {
        this.projectConfig = projectConfig;
        this.messagePattern = messagePattern;
        root.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 5));

        IntervalLabel firstInterval = new IntervalLabel();
        root.add(firstInterval);
        messagePattern.iterateOverSegments((seg, beforeInterval) -> {
            SegLabel segLabel = new SegLabel(seg);
            segLabel.before = beforeInterval;
            beforeInterval.after = segLabel;

            IntervalLabel afterInterval = new IntervalLabel();
            segLabel.after = afterInterval;
            afterInterval.before = segLabel;

            root.add(segLabel);
            root.add(afterInterval);
            return afterInterval;
        }, firstInterval);
    }

    void deleteSegment(SegLabel segLabel) {
        // TODO implement
    }

    private class SegLabel extends JLabel {
        private static final Border BORDER;
        private static final Border PRE_DEL_BORDER;
        static {
            RoundCornerBorder segBorder = new RoundCornerBorder(10, 1, IcUI.ACCENT_COLOR);
            JBEmptyBorder segPadding = JBUI.Borders.empty(5, 10);
            BORDER = JBUI.Borders.merge(segPadding, segBorder, true);

            RoundCornerBorder pdBorder = new RoundCornerBorder(10, 1, JBColor.RED);
            PRE_DEL_BORDER = JBUI.Borders.merge(segPadding, pdBorder, true);
        }

        IntervalLabel before;
        IntervalLabel after;
        private boolean preDeletion;

        public SegLabel(MessagePatternSeg segment) {
            setText(IcStringUtil.toHtml(segment.toHtmlString(), FONT, JBColor.BLACK));
            setBorder(BORDER);

            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        if (preDeletion) {
                            recover();
                            deleteSegment(SegLabel.this);
                        } else {
                            Point loc = getLocation(root.getLocation());
                            higher.showTooltip(IcConst.get("tooltip.deleteSegment"), loc, getWidth(), getHeight());
                            setBorder(PRE_DEL_BORDER);
                            preDeletion = true;
                        }
                    } else {
                        recover();
                    }
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    recover();
                    higher.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    recover();
                    higher.setCursor(Cursor.getDefaultCursor());
                }
            });
        }

        void recover() {
            if (preDeletion) {
                higher.hideTooltip();
                setBorder(BORDER);
                preDeletion = false;
            }
        }
    }

    private class IntervalLabel extends RoundCornerLabel {

        private SegLabel before;
        private SegLabel after;
        public IntervalLabel() {
            super(6);
            setFont(FONT);
            setText(" ");
            addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    super.mouseClicked(e);
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                    Point pointAtCanvas = getLocation(root.getLocation());
                    higher.showTooltip(IcConst.get("tooltip.addNewSegment"), pointAtCanvas, getWidth(), getHeight());
                    higher.setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    higher.hideTooltip();
                    higher.setCursor(Cursor.getDefaultCursor());
                }
            });
        }
    }
}
