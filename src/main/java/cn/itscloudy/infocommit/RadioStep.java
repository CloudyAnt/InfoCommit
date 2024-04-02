package cn.itscloudy.infocommit;

import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class RadioStep extends Step {
    private static final Color[] colors = new Color[]{
            JBColor.RED, JBColor.GREEN, JBColor.YELLOW, JBColor.BLUE, JBColor.MAGENTA, JBColor.CYAN, JBColor.ORANGE
    };

    private final String name;
    private final JPanel root;
    private final ButtonGroup group;

    RadioStep(String key, String name, String[] options, String defaultValue) {
        super(key);
        this.name = name;

        root = new JPanel();
        root.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 0));
        group = new ButtonGroup();

        boolean hasDefault = false;
        for (int i = 0; i < options.length; i++) {
            String radioName = options[i];
            JRadioButton radio = addRadio(i, radioName, group);
            if (radioName.equals(defaultValue)) {
                hasDefault = true;
                radio.setSelected(true);
            }
        }
        if (!hasDefault) {
            group.getElements().nextElement().setSelected(true);
        }
    }

    private JRadioButton addRadio(int index, String name, ButtonGroup group) {
        JRadioButton radio = new JRadioButton(name);
        radio.setForeground(colors[index >= colors.length ? colors.length - 1 : index]);
        radio.setActionCommand(name);
        root.add(radio);
        group.add(radio);
        return radio;
    }

    @Override
    @NotNull String getSegmentValue() {
        return group.getSelection().getActionCommand();
    }

    @Override
    @NotNull
    JLabel getConfigLabel() {
        JLabel label = createConfigLabel(name, JBColor.WHITE, Display.BORDER_COLOR);
        label.setOpaque(true);
        label.setBackground(Display.ACCENT_COLOR);
        return label;
    }

    @Override
    @NotNull FormStep getFormStep() {
        return new FormStep(name, root);
    }
}
