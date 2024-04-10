package cn.itscloudy.infocommit;

import lombok.Getter;

import javax.swing.*;

public class FormStep {
    @Getter
    private JPanel root;
    private JLabel name;
    private JPanel contentHolder;

    FormStep(String name, JComponent content) {
        this.name.setText(name);
        this.contentHolder.setLayout(IcUI.FULL_LAYOUT);
        this.contentHolder.add(content);
    }
}
