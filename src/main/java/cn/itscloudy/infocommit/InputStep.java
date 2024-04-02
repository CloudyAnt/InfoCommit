package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.util.TextPrompt;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBTextField;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class InputStep extends Step {

    private final String name;
    private String defaultValue;
    private final JTextField field;

    InputStep(String key, String name, String defaultValue) {
        super(key);
        this.name = name;
        this.field = new JBTextField();
        updateDefaultValue(defaultValue);
    }

    @Override
    public void afterAccepted() {
        String text = field.getText();
        if (!text.isEmpty()) {
            field.setText("");
            updateDefaultValue(text);
        }
    }

    private void updateDefaultValue(String value) {
        defaultValue = value;
        field.removeAll();
        TextPrompt tp = new TextPrompt(" " + defaultValue, field);
        tp.setForeground(JBColor.GRAY);
    }

    @Override
    @NotNull String getSegmentValue() {
        String text = field.getText();
        if (text.isEmpty()) {
            return defaultValue;
        }
        return text;
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
        return new FormStep(name, field);
    }
}
