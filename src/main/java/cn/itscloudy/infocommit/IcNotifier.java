package cn.itscloudy.infocommit;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

public class IcNotifier {
    private static final String GROUP_ID = "cn.itscloudy.flowcommit.notifications";

    private IcNotifier() {
    }

    public static void info(@NotNull Project project, @NotNull String content) {
        new Notification(GROUP_ID, IcConst.ID, content, NotificationType.INFORMATION).notify(project);
    }

    public static void info(@NotNull Project project, String title, @NotNull String content) {
        if (StringUtils.isNotBlank(title)) {
            title = IcConst.ID + ": " + title;
        } else {
            title = IcConst.ID;
        }
        new Notification(GROUP_ID, title, content, NotificationType.INFORMATION).notify(project);
    }

    public static void warn(@NotNull Project project, @NotNull String content) {
        new Notification(GROUP_ID, IcConst.ID, content, NotificationType.WARNING).notify(project);
    }

    public static void error(@NotNull Project project, @NotNull String content) {
        new Notification(GROUP_ID, IcConst.ID, content, NotificationType.ERROR).notify(project);
    }
}
