package cn.itscloudy.infocommit.step;

import cn.itscloudy.infocommit.util.TextPrompt;
import com.intellij.ui.JBColor;
import lombok.Getter;

import javax.swing.*;

public class InputStep implements CommitStep {
    private JTextField field;
    @Getter
    private JPanel root;
    @Getter
    private final String key;
    private final String name;
    private String defaultValue;
    private StepSegmentImp stepSegment;

    public InputStep(String key, String name, String defaultValue) {
        this.key = key;
        this.name = name;
        updateDefaultValue(defaultValue);
    }

    public StepSegment getStepSegment() {
        if (stepSegment == null) {
            stepSegment = new StepSegmentImp();
        }
        return stepSegment;
    }

    @Override
    public void accept() {
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

    private class StepSegmentImp extends StepSegment {

        StepSegmentImp() {
            super(name, root);
        }

        @Override
        public String getSegmentValue() {
            String text = field.getText();
            if (text.isEmpty()) {
                return defaultValue;
            }
            return text;
        }
    }
}
