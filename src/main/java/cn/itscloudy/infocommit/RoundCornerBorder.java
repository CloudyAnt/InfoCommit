package cn.itscloudy.infocommit;

import com.intellij.util.ui.JBUI;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Objects;

public class RoundCornerBorder extends AbstractBorder {
    private final int arc;
    private final int thickness;
    private final Color borderColor;
    private final Color cornerColor;

    public RoundCornerBorder(int arc, int thickness, Color borderColor) {
        this(arc, thickness, borderColor, null);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return JBUI.insets(insets.top + thickness, insets.left + thickness,
                insets.bottom + thickness, insets.right + thickness);
    }

    public RoundCornerBorder(int arc, int thickness, Color borderColor, Color cornerColor) {
        assert arc >= 0;
        assert thickness >= 1;
        this.arc = arc;
        this.thickness = thickness;
        this.borderColor = borderColor;
        this.cornerColor = cornerColor;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int r = arc / 2;
        int w = thickness == 1 ? width - 1 : width;
        int h = thickness == 1 ? height - 1 : height;

        Area outer = getRoundRectangle(w, h, r, 0);
        if (c instanceof JPopupMenu) {
            g2.setPaint(c.getBackground());
            g2.fill(outer);
        } else {
            Container parent = c.getParent();
            if (Objects.nonNull(parent)) {
                // 将四角绘制为父组件色
                Color cornerColor = this.cornerColor == null ? parent.getBackground() : this.cornerColor;
                g2.setPaint(cornerColor);
                Area corner = new Area(new Rectangle2D.Float(x, y, width, height));
                corner.subtract(outer);
                g2.fill(corner);
            }
        }

        if (thickness == 1) {
            g2.setPaint(borderColor);
            g2.setStroke(new BasicStroke(thickness));
            g2.draw(outer);
        } else {
            Area inner = getRoundRectangle(w, h, r, thickness);
            outer.subtract(inner);
            g2.setPaint(borderColor);
            g2.fill(outer);
        }
        g2.dispose();
    }

    private Area getRoundRectangle(int width, int height, int radius, int offset) {
        radius = radius - offset;
        GeneralPath path = new GeneralPath();
        path.moveTo(radius + offset, offset);
        path.quadTo(offset, offset, offset, radius + offset);

        int bottom = height - offset;
        path.lineTo(offset, height - radius - offset);
        path.quadTo(offset, bottom, radius + offset, bottom);

        int right = width - offset;
        path.lineTo(right - radius, bottom);
        path.quadTo(right, bottom, right, bottom - radius);

        path.lineTo(right, radius + offset);
        path.quadTo(right, offset, right - radius, offset);

        path.closePath();
        return new Area(path);
    }
}
