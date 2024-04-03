package cn.itscloudy.infocommit.config;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

public class ConfigCanvasLowerLayer {

    private final ProjectConfig projectConfig;
    @Getter
    private JPanel root;

    public ConfigCanvasLowerLayer(ProjectConfig projectConfig) {
        this.projectConfig = projectConfig;
        root.setLayout(new FlowLayout(FlowLayout.LEFT));
    }
}
