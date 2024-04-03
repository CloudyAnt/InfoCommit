package cn.itscloudy.infocommit.config;

import lombok.Getter;

import javax.swing.*;

public class ConfigCanvasHigherLayer {

    private final ProjectConfig projectConfig;
    @Getter
    private JPanel root;

    public ConfigCanvasHigherLayer(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
    }
}
