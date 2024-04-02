package cn.itscloudy.infocommit;

import cn.itscloudy.infocommit.context.IcProjectContext;
import cn.itscloudy.infocommit.context.IcProjectContextManager;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vcs.CheckinProjectPanel;
import com.intellij.openapi.vcs.changes.CommitContext;
import com.intellij.openapi.vcs.checkin.CheckinHandler;
import com.intellij.openapi.vcs.checkin.CheckinHandlerFactory;
import com.intellij.util.keyFMap.KeyFMap;
import org.jetbrains.annotations.NotNull;

public class IcCheckinHandlerFactory extends CheckinHandlerFactory {
    @Override
    @NotNull
    public CheckinHandler createHandler(@NotNull CheckinProjectPanel panel, @NotNull CommitContext commitContext) {
        return new CheckinHandler() {
            @Override
            public ReturnResult beforeCheckin() {
                IcProjectContext projectContext = IcProjectContextManager.getInstance(panel.getProject());
                if (projectContext != null) {
                    Display display = projectContext.getDisplay();
                    if (display != null && !display.isAmendMode()) {
                        String message = display.instrumentMessage(panel.getCommitMessage());
                        panel.setCommitMessage(message);
                        display.updateCache();
                        return ReturnResult.COMMIT;
                    }
                }
                return ReturnResult.COMMIT;
            }
        };
    }
}
