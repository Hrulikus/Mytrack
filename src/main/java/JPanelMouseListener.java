import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.logging.Logger;

import static java.awt.Cursor.*;

public class JPanelMouseListener implements MouseListener, MouseMotionListener {

    Logger logger = Logger.getLogger(this.getClass().getName());
    PointsPanel parentPanel;
    int inX = -1;
    int inY = -1;

    JPanelMouseListener (PointsPanel panel) {
        this.parentPanel = panel;
    }

    @Override
    public void mouseDragged (MouseEvent e) {
        logger.info("method called");
        logger.info("Mouse dragged");
        int newHeight;
        switch (parentPanel.getCursor().getType()) {
            case NW_RESIZE_CURSOR:
                newHeight = parentPanel.setSize(parentPanel.getWidth() - e.getX());
                parentPanel.setLocation(parentPanel.getX() + e.getX(),
                                        parentPanel.getY() + parentPanel.getHeight() - newHeight);
                parentPanel.locatePoints();
                parentPanel.repaint();
                return;
            case NE_RESIZE_CURSOR:
                newHeight = parentPanel.setSize(e.getX());
                parentPanel.setLocation(parentPanel.getX(),
                                        parentPanel.getY() + parentPanel.getHeight() - newHeight);
                parentPanel.locatePoints();
                parentPanel.repaint();
                return;
            case SW_RESIZE_CURSOR:
                int deltaX = parentPanel.getWidth() - e.getX();
                parentPanel.setSize(deltaX);
                parentPanel.setLocation(parentPanel.getX() + e.getX(), parentPanel.getY());
                parentPanel.locatePoints();
                parentPanel.repaint();
                return;
            case SE_RESIZE_CURSOR:
                parentPanel.setSize(e.getX());
                parentPanel.locatePoints();
                parentPanel.repaint();
                return;
        }
        logger.info(inX + " -> " + e.getX() + "; " + inY + " -> " + e.getY());
        parentPanel.movePanel(e.getX() - inX, e.getY() - inY);
    }

    @Override
    public void mouseMoved (MouseEvent e) {
        if (e.getX() > -5 &&
                    e.getX() < 5 &&
                    e.getY() > -5 &&
                    e.getY() < 5)
            parentPanel.setCursor(new Cursor(NW_RESIZE_CURSOR));
        else if (e.getX() > (parentPanel.getWidth() - 5) &&
                         e.getX() < (parentPanel.getWidth() + 5) &&
                         e.getY() > -5 &&
                         e.getY() < 5)
            parentPanel.setCursor(new Cursor(NE_RESIZE_CURSOR));
        else if (e.getX() > -5 &&
                         e.getX() < 5 &&
                         e.getY() > parentPanel.getHeight() - 5 &&
                         e.getY() < parentPanel.getHeight() + 5)
            parentPanel.setCursor(new Cursor(SW_RESIZE_CURSOR));
        else if (e.getX() > (parentPanel.getWidth() - 5) &&
                         e.getX() < (parentPanel.getWidth() + 5) &&
                         e.getY() > parentPanel.getHeight() - 5 &&
                         e.getY() < parentPanel.getHeight() + 5)
            parentPanel.setCursor(new Cursor(SE_RESIZE_CURSOR));
        else
            parentPanel.setCursor(Cursor.getDefaultCursor());
    }

    @Override
    public void mouseClicked (MouseEvent e) {
        logger.info("method called");
        parentPanel.changeActiveFlag();
    }

    @Override
    public void mousePressed (MouseEvent e) {
        logger.info("method called");
        inX = e.getX();
        inY = e.getY();
        logger.info("Mouse pressed. Saved position: (" + inX + ";" + inY + ")");
    }

    @Override
    public void mouseReleased (MouseEvent e) {
    }

    @Override
    public void mouseEntered (MouseEvent e) {
    }

    @Override
    public void mouseExited (MouseEvent e) {
    }
}
