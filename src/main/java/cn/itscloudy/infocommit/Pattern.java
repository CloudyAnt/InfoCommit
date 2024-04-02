package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.util.SwingUtil;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class Pattern {

    private static final String[] DEF_SEGMENT_EXPRESSIONS = new String[]{
            "[NAME]Name/姓名:$(git config user.email)",
            " ",
            "[TYPE]Type/类型:refactor fix feat chore doc test style",
            ": ",
            IcConst.MESSAGE_SIGN
    };

    private final Seg first;

    Pattern(Project project, StepCacheResolver cacheResolver) {
        this.first = Seg.resolve(project, cacheResolver, DEF_SEGMENT_EXPRESSIONS[0]);
        Seg current = first;
        for (int i = 1; i < DEF_SEGMENT_EXPRESSIONS.length; i++) {
            Seg next = Seg.resolve(project, cacheResolver, DEF_SEGMENT_EXPRESSIONS[i]);
            current.setNext(next);
            next.setPrev(current);
            current = next;
        }
    }

    @NotNull
    public String toDisplayString() {
        String segStyle = SwingUtil.fontToCssStyles(IcConst.DEF_FONT, JBColor.BLACK);
        String messageSignStyle = SwingUtil.fontToCssStyles(IcConst.DEF_FONT, Display.ACCENT_COLOR);

        StringBuilder sb = new StringBuilder("<html><body style=\"" + segStyle + "\">");
        iterateOverSegments(current -> {
            if (current.isCommitMessage()) {
                sb.append("<span style=\"").append(messageSignStyle).append("\">").append(IcConst.MESSAGE_SIGN)
                        .append("</span>");
                return;
            }
            sb.append(current.getSegmentValue());
        });
        return sb.append("</body></html>").toString();
    }

    public void acceptAll() {
        iterateOverSegments(s -> {
            if (s instanceof Step step) {
                step.afterAccepted();
            }
        });
    }

    public String instrument(String message) {
        StringBuilder sb = new StringBuilder();
        iterateOverSegments(segment -> {
            if (segment.isCommitMessage()) {
                sb.append(message);
                return;
            }
            sb.append(segment.getSegmentValue());
        });
        return sb.toString();
    }

    public void iterateOverSegments(Consumer<Seg> consumer) {
        Seg current = first;
        do {
            consumer.accept(current);
            current = current.getNext();
        } while (current != null);
    }

    public void iterateOverSteps(Consumer<Step> consumer) {
        Seg current = first;
        do {
            if (current instanceof Step step) {
                consumer.accept(step);
            }
            current = current.getNext();
        } while (current != null);
    }
}
