package cn.itscloudy.infocommit.util;

import java.awt.*;

public class IcStringUtil {
    private IcStringUtil() {
    }

    public static String toHtml(String display, Font font, Color fg) {
        String segStyle = SwingUtil.fontToCssStyles(font, fg);
        return "<html><body style=\"" + segStyle + "\">" + display + "</body></html>";
    }

}
