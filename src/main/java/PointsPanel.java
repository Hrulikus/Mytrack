import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.logging.Logger;

import static java.awt.Color.RED;
import static java.awt.Color.WHITE;

class PointsPanel extends JPanel {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private Points POINTS = Points.EMPTY;
    private boolean isActive = false;
    Border activeBorder = BorderFactory.createDashedBorder(Color.GREEN, 3, 2);


    PointsPanel (Dimension d) {
        logger.info("method called");
        setSize(d);
        setBackground(WHITE);
        setOpaque(false);
        setLayout(null);
        setVisible(true);
        setLocation(0, 0);
        JPanelMouseListener listener = new JPanelMouseListener(this);
        addMouseListener(listener);
        addMouseMotionListener(listener);
    }

    public void locatePoints () {
        logger.info("method called");
        POINTS.locatePointsAtPanel(this);
    }

    public void setSize (int width, int height) {
        super.setSize(width, height);
        logger.info("method called");
        if (POINTS.size() > 0) {
            locatePoints();
            repaint();
        }
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        logger.info("method called");
        if (POINTS.size() > 0) {
            logger.info("already drawn is false and Points.size() > 0");
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(RED);
            g2.setStroke(new BasicStroke(3));
            for (int i = 0; i < POINTS.size() - 1; i++) {
                g2.drawLine((int) POINTS.get(i).getPanelX(), (int) (POINTS.get(i).getPanelY()),
                            (int) POINTS.get(i + 1).getPanelX(), (int) (POINTS.get(i + 1).getPanelY()));
            }
        }
    }

    public void changeActiveFlag () {
        logger.info("method called");
        isActive = !isActive;
        if (isActive)
            setBorder(activeBorder);
        else
            setBorder(null);
    }

    public Points getPoints () {
        logger.info("method called");
        return POINTS;
    }

    public void movePanel (int x, int y) {
        logger.info("method called");
        java.awt.Point positionBeforeMove = getLocation();
        setLocation((int) positionBeforeMove.getX() + x,
                    (int) positionBeforeMove.getY() + y);
    }
}
