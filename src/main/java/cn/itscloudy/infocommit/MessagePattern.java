package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.step.CommitStep;
import cn.itscloudy.infocommit.step.InputStep;
import cn.itscloudy.infocommit.step.RadioStep;
import cn.itscloudy.infocommit.util.CmdUtil;
import com.intellij.openapi.project.Project;
import lombok.Getter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public abstract class MessagePattern {
    private final List<MessagePatternSegment> segments = new ArrayList<>();
    @Getter
    private final List<CommitStep> steps = new ArrayList<>();
    private final Project project;
    private final StepCacheResolver cacheResolver;

    MessagePattern(Project project, StepCacheResolver cacheResolver) {
        this.project = project;
        this.cacheResolver = cacheResolver;
        for (String seg : getSegments()) {
            String segKey = findSegKey(seg);
            if (segKey != null) {
                resolveStep(segKey, seg.substring(segKey.length() + 2));
            } else {
                segments.add(new TextSegment(seg));
            }
        }
    }

    abstract String[] getSegments();

    private String findSegKey(String seg) {
        if (seg.startsWith("[")) {
            int segKeyEnd = seg.indexOf("]");
            if (segKeyEnd > 0) {
                return seg.substring(1, segKeyEnd);
            }
        }
        return null;
    }

    private void resolveStep(String key, String seg) {
        String defaultValue = cacheResolver.get(key);

        String[] nameOptionSplit = seg.split(":");
        String[] nameSplit = nameOptionSplit[0].split("/");
        String lang = Locale.getDefault().getLanguage().equals("zh") ? "zh" : "en";
        String name = nameSplit[0];
        if (lang.equals("zh")) {
            name = nameSplit.length > 1 ? nameSplit[1] : nameSplit[0];
        }

        String optionsString;
        if (nameOptionSplit.length == 1) {
            optionsString = "";
        } else {
            optionsString = nameOptionSplit[1].trim();
            if (optionsString.startsWith("$(") && optionsString.endsWith(")")) {
                if (defaultValue == null) {
                    // read default value by command
                    try {
                        String[] command = optionsString.substring(2, optionsString.length() - 1).split(" ");
                        CmdUtil.Result run = CmdUtil.run(Arrays.stream(command).collect(Collectors.toList()));
                        if (run.getExitValue() == 0) {
                            defaultValue = run.getOutput();
                        }
                    } catch (IOException e) {
                        IcNotifier.error(project, "Failed to resolve default value for " + key +
                                ":" + e.getMessage());
                    }
                }

                optionsString = "";
            }
        }

        String[] options = optionsString.split(" ");
        CommitStep commitStep;
        if (options.length > 1) {
            commitStep = new RadioStep(key, name, options, defaultValue == null ? options[0] : defaultValue);
        } else if (options.length == 1) {
            commitStep = new InputStep(key, name, defaultValue == null ? options[0] : defaultValue);
        } else {
            commitStep = new InputStep(key, name, defaultValue == null ? "Unknown" : defaultValue);
        }
        steps.add(commitStep);
        segments.add(commitStep.getStepSegment());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (MessagePatternSegment segment : segments) {
            sb.append(segment.getSegmentValue());
        }
        return sb.toString();
    }

    private static class TextSegment implements MessagePatternSegment {
        private final String value;

        TextSegment(String value) {
            this.value = value;
        }

        @Override
        public String getSegmentValue() {
            return value;
        }
    }
}
