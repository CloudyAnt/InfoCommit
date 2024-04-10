package cn.itscloudy.infocommit;

import com.intellij.ui.Gray;
import com.intellij.ui.components.panels.VerticalLayout;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class Form {
    @Getter
    private JPanel root;
    private JPanel steps;
    private JLabel bottomLabel;
    private final MessagePattern messagePattern;

    Form(@NotNull MessagePattern messagePattern) {
        this.messagePattern = messagePattern;
        steps.setLayout(new VerticalLayout(10));
        bottomLabel.setText(IcConst.get("providedBy"));
        bottomLabel.setForeground(Gray.x7F);
        updateSteps();
    }

    private void updateSteps() {
        steps.removeAll();

        AtomicBoolean firstStep = new AtomicBoolean(true);
        messagePattern.iterateOverSteps(step -> {
            if (firstStep.get()) {
                firstStep.set(false);
            } else {
                steps.add(new JSeparator());
            }
            FormStep formStep = step.getFormStep();
            steps.add(formStep.getRoot());
        });
        root.updateUI();
    }
}
