package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.step.CommitStep;
import cn.itscloudy.infocommit.step.StepSegment;
import com.intellij.ui.components.panels.VerticalLayout;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class PrefixConfig {
    @Getter
    private JPanel root;
    private final DefPrefixPattern defPrefixPattern;

    public PrefixConfig(@NotNull DefPrefixPattern defPrefixPattern) {
        this.defPrefixPattern = defPrefixPattern;
        root.setLayout(new VerticalLayout(10));
        updateSteps();
    }

    private void updateSteps() {
        root.removeAll();
        boolean first = true;
        for (CommitStep step : defPrefixPattern.getSteps()) {
            StepSegment stepSegment = step.getStepSegment();
            if (first) {
                stepSegment.hideSeparator();
                first = false;
            }
            root.add(stepSegment.getRoot());
        }
        root.updateUI();
    }

    String getPrefix() {
        return defPrefixPattern.toString();
    }
}
