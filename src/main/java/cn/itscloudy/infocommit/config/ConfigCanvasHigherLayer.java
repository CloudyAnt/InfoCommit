package cn.itscloudy.infocommit.config;

import lombok.Getter;

import javax.swing.*;

public class ConfigCanvasHigherLayer {

    private final Config config;
    @Getter
    private JPanel root;

    public ConfigCanvasHigherLayer(Config config) {
        this.config = config;
    }
}
