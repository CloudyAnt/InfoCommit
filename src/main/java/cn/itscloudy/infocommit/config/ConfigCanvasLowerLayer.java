package cn.itscloudy.infocommit.config;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class ConfigCanvasLowerLayer {

    private final Config config;
    @Getter
    private JPanel root;

    public ConfigCanvasLowerLayer(Config config) {
        this.config = config;
        root.setLayout(new FlowLayout(FlowLayout.LEFT));
    }
}
