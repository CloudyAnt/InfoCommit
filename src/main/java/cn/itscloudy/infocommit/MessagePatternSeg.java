package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.ui.RoundCornerBorder;
import cn.itscloudy.infocommit.util.CmdUtil;
import cn.itscloudy.infocommit.util.SwingUtil;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.stream.Collectors;

@Getter
@Setter
public abstract class MessagePatternSeg {

    MessagePatternSeg prev;
    MessagePatternSeg next;

    public boolean isInputMessage() {
        return false;
    }

    @NotNull
    abstract JLabel getConfigLabel();

    public void afterAccepted() {
    }

    abstract String getSegmentValue();

    public String toHtmlString() {
        String color = SwingUtil.color2Hex(IcUI.SHADDW_COLOR);
        return getSegmentValue()
                .replace(" ", "<span style=\"background-color: " + color + "\">&nbsp;</span>");
    }

    protected static JLabel createConfigLabel(String text, Color foreground, Color borderColor) {
        JLabel l = new JLabel(text);
        l.setFont(IcUI.DEF_FONT);
        l.setBorder(new RoundCornerBorder(10, 1, borderColor));
        l.setForeground(foreground);
        return l;
    }

    public static MessagePatternSeg resolve(Project project, StepCacheResolver cacheResolver, String expression) {
        if (expression.equals(IcConst.MESSAGE_SIGN)) {
            return new InputMessagePatternSeg();
        }
        String key = findSegKey(expression);
        if (key == null) {
            return new TextMessagePatternSeg(expression);
        }

        String defaultValue = cacheResolver.get(key);

        expression = expression.substring(key.length() + 2); // 2 for []
        String[] nameOptionSplit = expression.split(":");
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
                        if (run.exitValue() == 0) {
                            defaultValue = run.output();
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
        return Step.resolve(key, name, options, defaultValue);
    }

    private static String findSegKey(String seg) {
        if (seg.startsWith("[")) {
            int segKeyEnd = seg.indexOf("]");
            if (segKeyEnd > 0) {
                return seg.substring(1, segKeyEnd);
            }
        }
        return null;
    }

    private static class InputMessagePatternSeg extends MessagePatternSeg {
        @Override
        public boolean isInputMessage() {
            return true;
        }

        @Override
        @NotNull
        JLabel getConfigLabel() {
            return createConfigLabel(IcConst.MESSAGE_SIGN, IcUI.ACCENT_COLOR, IcUI.BORDER_COLOR);
        }

        @Override
        String getSegmentValue() {
            return IcConst.MESSAGE_SIGN;
        }

        @Override
        public String toHtmlString() {
            String color = SwingUtil.color2Hex(IcUI.ACCENT_COLOR);
            return "<span style=\"color: " + color + "\">" + IcConst.MESSAGE_SIGN + "</span>";
        }
    }

    private static class TextMessagePatternSeg extends MessagePatternSeg {
        private final String text;

        TextMessagePatternSeg(String text) {
            this.text = text;
        }

        @Override
        @NotNull
        JLabel getConfigLabel() {
            return createConfigLabel(text, JBColor.WHITE, JBColor.GRAY);
        }

        @Override
        String getSegmentValue() {
            return text;
        }
    }
}
