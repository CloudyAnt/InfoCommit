package cn.itscloudy.infocommit.config;

import cn.itscloudy.infocommit.IcUI;
import cn.itscloudy.infocommit.ui.ArrowBubbleBorder;
import cn.itscloudy.infocommit.util.SwingUtil;
import com.intellij.util.ui.JBUI;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;

public class ProjectConfigCanvasHigher {

    private final ProjectConfig projectConfig;
    private final Tooltip tooltip;
    @Getter
    private JPanel root;
    ProjectConfigCanvasLower lower;

    public ProjectConfigCanvasHigher(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
        root.setLayout(null);
        tooltip = new Tooltip();
        root.add(tooltip);
        tooltip.setVisible(false);
    }

    public void showTooltip(String text, Point p, int cw, int ch) {
        int tw = tooltip.fm.stringWidth(text);
        tooltip.setText(text);

        Point pinPoint = new Point(cw / 2 + p.x, ch + p.y);
        int toolTipWidth = tw + Tooltip.H_PADDING;
        int halfToolTipWidth = toolTipWidth / 2;
        if (pinPoint.x > halfToolTipWidth) {
            tooltip.border.setDirection(ArrowBubbleBorder.Direction.UP);
            tooltip.setBounds(pinPoint.x - halfToolTipWidth, pinPoint.y + Tooltip.ARROW_HEIGHT,
                    toolTipWidth, Tooltip.HEIGHT);
        } else {
            pinPoint = new Point(cw + p.x, ch / 2 + p.y);
            tooltip.border.setDirection(ArrowBubbleBorder.Direction.LEFT);
            tooltip.setBounds(pinPoint.x + Tooltip.ARROW_HEIGHT, pinPoint.y - tooltip.getHeight() / 2,
                    toolTipWidth, Tooltip.HEIGHT);
        }
        tooltip.setVisible(true);
    }

    void hideTooltip() {
        tooltip.setVisible(false);
    }

    void setCursor(Cursor cursor) {
        root.setCursor(cursor);
    }

    private static class Tooltip extends JLabel {
        private static final int ARROW_HEIGHT = 5;
        private static final int H_PADDING = 10;
        private static final int HEIGHT = 28;
        private final FontMetrics fm;
        private final ArrowBubbleBorder border =
                new ArrowBubbleBorder(ArrowBubbleBorder.Direction.UP, ARROW_HEIGHT);


        Tooltip() {
            setFont(IcUI.DEF_FONT);
            fm = getFontMetrics(IcUI.DEF_FONT);

            setHorizontalAlignment(SwingConstants.CENTER);
            setVerticalAlignment(SwingConstants.CENTER);
            setBorder(border);
            setOpaque(true);
            setBackground(JBUI.CurrentTheme.ToolWindow.background());
        }

        @Override
        protected void paintComponent(Graphics g) {
            ArrowBubbleBorder.Direction direction = border.getDirection();
            int w = getWidth();
            int h = getHeight();
            Area interior = SwingUtil.getRoundRectangle(w, h, 6, 0);
            if (direction.xOff != 0 || direction.yOff != 0) {
                interior.transform(AffineTransform.getTranslateInstance(
                        direction.xOff * ARROW_HEIGHT,
                        direction.yOff * ARROW_HEIGHT));
            }

            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fill(interior);

            String text = getText();
            int tw = fm.stringWidth(text);
            int th = fm.getHeight();
            Insets bi = border.getBorderInsets(this);
            g2.setColor(getForeground());

            int descent = fm.getDescent();
            g2.drawString(text, (w + bi.left - bi.right - tw) / 2, (h + bi.top - bi.bottom + th) / 2 - descent);
            g2.dispose();
        }
    }
}
