package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.util.IcStringUtil;
import com.google.common.base.Charsets;
import com.intellij.openapi.project.Project;
import com.intellij.ui.JBColor;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;

public class MessagePattern {

    private static final List<String> DEF_SEGMENT_EXPRESSIONS = new ArrayList<>() {{
        add("[NAME]Name/姓名:$(git config user.email)");
        add(" ");
        add("[TYPE]Type/类型:refactor fix feat chore doc test style");
        add(": ");
        add(IcConst.MESSAGE_SIGN);
    }};

    private MessagePatternSeg first;
    private List<String> expressions;
    private final Project project;
    private final StepCacheResolver cacheResolver;

    MessagePattern(Project project, StepCacheResolver cacheResolver) {
        this.project = project;
        this.cacheResolver = cacheResolver;
        List<String> expressions = loadPatternExpressions(project);
        if (expressions == null || expressions.isEmpty()) {
            expressions = DEF_SEGMENT_EXPRESSIONS;
        }
        updateExpressions(expressions);
    }

    private List<String> loadPatternExpressions(Project project) {
        IcProjectContext context = IcProjectContextManager.getInstance(project);
        if (context.isValid()) {
            String patternFilePathString = context.getBasePath() + "/" + IcConst.PATTEN_FILE_NAME;
            Path path = Path.of(patternFilePathString);
            if (Files.exists(path)) {
                try {
                    return Files.readAllLines(path, Charsets.UTF_8);
                } catch (IOException e) {
                    IcNotifier.error(project, "Failed to load pattern file: " + e.getMessage());
                }
            }
        }
        return null;
    }

    void updateExpressions(List<String> expressions) {
        // ! make sure expressions are not empty

        this.expressions = expressions;
        this.first = MessagePatternSeg.resolve(project, cacheResolver, expressions.get(0));
        MessagePatternSeg current = first;
        for (int i = 1; i < DEF_SEGMENT_EXPRESSIONS.size(); i++) {
            MessagePatternSeg next = MessagePatternSeg.resolve(project, cacheResolver, expressions.get(i));
            current.setNext(next);
            next.setPrev(current);
            current = next;
        }
    }

    @NotNull
    public String toDisplayString() {
        StringBuilder sb = new StringBuilder();
        iterateOverSegments(seg -> sb.append(seg.toHtmlString()));
        return IcStringUtil.toHtml(sb.toString(), IcUI.DEF_FONT, JBColor.BLACK);
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
            if (segment.isInputMessage()) {
                sb.append(message);
                return;
            }
            sb.append(segment.getSegmentValue());
        });
        return sb.toString();
    }

    public <T> T iterateOverSegments(BiFunction<MessagePatternSeg, T, T> consumer, T t) {
        MessagePatternSeg current = first;
        do {
            t = consumer.apply(current, t);
            current = current.getNext();
        } while (current != null);
        return t;
    }


    public void iterateOverSegments(Consumer<MessagePatternSeg> consumer) {
        MessagePatternSeg current = first;
        do {
            consumer.accept(current);
            current = current.getNext();
        } while (current != null);
    }

    public void iterateOverSteps(Consumer<Step> consumer) {
        MessagePatternSeg current = first;
        do {
            if (current instanceof Step step) {
                consumer.accept(step);
            }
            current = current.getNext();
        } while (current != null);
    }

    public boolean isDifferentFrom(List<String> expressions) {
        if (this.expressions.size() != expressions.size()) {
            return true;
        }

        for (int i = 0; i < this.expressions.size(); i++) {
            if (!this.expressions.get(i).equals(expressions.get(i))) {
                return true;
            }
        }

        return false;
    }
}
