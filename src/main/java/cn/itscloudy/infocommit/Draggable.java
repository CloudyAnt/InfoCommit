package cn.itscloudy.infocommit;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Draggable {

    public static void make(Component targetComp, Component listeningComp, Component boundaryComp,
                            Runnable onNotDragged) {
        Listener l = new Listener(targetComp, boundaryComp, onNotDragged);
        listeningComp.addMouseListener(l);
        listeningComp.addMouseMotionListener(l);
    }

    private static class Listener extends MouseAdapter {
        private final Component targetComp;
        private final Component boundaryComp;
        private final Runnable onNotDragged;
        private Point compLocation;
        private Point mouseLocation;
        int xa = -1, xb = 10000, ya = -1, yb = 10000;

        private Listener(Component targetComp, Component boundaryComp, Runnable onNotDragged) {
            this.targetComp = targetComp;
            this.boundaryComp = boundaryComp;
            this.onNotDragged = onNotDragged;
        }

        public void mousePressed(MouseEvent e) {
            compLocation = targetComp.getLocation();
            mouseLocation = e.getLocationOnScreen();

            Point limitStart = new Point(0, 0);
            Point limitEnd = new Point(boundaryComp.getWidth() - targetComp.getWidth(),
                    boundaryComp.getHeight() - targetComp.getHeight());
            if (limitStart.x < limitEnd.x) {
                xa = limitStart.x;
                xb = limitEnd.x;
            } else {
                xa = limitEnd.x;
                xb = limitStart.x;
            }
            if (limitStart.y < limitEnd.y) {
                ya = limitStart.y;
                yb = limitEnd.y;
            } else {
                ya = limitEnd.y;
                yb = limitStart.y;
            }
        }

        public void mouseDragged(MouseEvent e) {
            Point mPresent = e.getLocationOnScreen();
            int x = mPresent.x - mouseLocation.x + compLocation.x, y = mPresent.y - mouseLocation.y + compLocation.y;
            targetComp.setLocation(new Point(x < xa ? xa : Math.min(x, xb), y < ya ? ya : Math.min(y, yb)));
        }

        public void mouseReleased(MouseEvent e) {
            Point currentLocation = targetComp.getLocation();
            if (Math.abs(compLocation.x - currentLocation.x) < 5 && Math.abs(compLocation.y - currentLocation.y) < 5) {
                onNotDragged.run();
            }
        }
    }
}
