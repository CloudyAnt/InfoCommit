package cn.itscloudy.infocommit;

import com.intellij.openapi.project.Project;
import lombok.Getter;

@Getter
public class DefPrefixPattern extends MessagePattern {
    private static final String[] DEF_SEGMENTS = new String[]{
            "[NAME]Name/姓名:$(git config user.email)",
            " ",
            "[TYPE]Type/类型:refactor fix feat chore doc test style",
            ": ",
    };

    DefPrefixPattern(Project project, StepCacheResolver cacheResolver) {
        super(project, cacheResolver);
    }

    @Override
    String[] getSegments() {
        return DEF_SEGMENTS;
    }

}
