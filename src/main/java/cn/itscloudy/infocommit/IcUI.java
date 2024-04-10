package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.util.SwingUtil;

import java.awt.*;

public interface IcUI {
    Color ACCENT_COLOR = SwingUtil.decodeColor("#CA8265", "#4E94DA");
    Color SHADDW_COLOR = SwingUtil.decodeColor("#909090", "#808080");
    Color BORDER_COLOR = SwingUtil.decodeColor("#F8A662", "#6391F9");

    Font DEF_FONT = new Font(null, Font.PLAIN, 12);

    GridLayout FULL_LAYOUT = new GridLayout(1, 1);
}
