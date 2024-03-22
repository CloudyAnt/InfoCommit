package cn.itscloudy.infocommit.step;

import javax.swing.*;

public interface CommitStep {

    StepSegment getStepSegment();

    JPanel getRoot();

    String getKey();

    default void accept() {
    }
}
