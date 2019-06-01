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


    PointsPanel () {
        logger.info("method called");
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

    public void locatePointsForFirstTime (DrawingPanel panel) {
        logger.info("method called");
        double ratio = getSizeRatio();
        int preferredWidth = panel.getWidth() / 2;
        Dimension newSize = new Dimension(preferredWidth, (int) (ratio * preferredWidth));
        this.setSize(newSize);
        POINTS.locatePointsAtPanel(newSize);
    }

    public double getSizeRatio () {
        logger.info("method called");
        logger.info("MaxPoint: " + POINTS.getMaximum().getX() + " " + POINTS.getMaximum().getY());
        logger.info("MinPoint: " + POINTS.getMinimum().getX() + " " + POINTS.getMinimum().getY());
        double spreadX = POINTS.getMaximum().getX() - POINTS.getMinimum().getX();
        double spreadY = POINTS.getMaximum().getY() - POINTS.getMinimum().getY();
        logger.info("Calculated spread: " + spreadX + " " + spreadY);

        double newSpreadX = spreadX * 10000;
        double newSpreadY = spreadY * 10000;
        if (spreadX != 0d) {
            logger.info("Calculated ratio: " + spreadY / spreadX);
            return newSpreadY / newSpreadX;
        }
        return 0d;
    }

    public int setSize (int width) {
        logger.info("method called");
        int height = (int) (width * getSizeRatio());
        super.setSize(width, height);
        return height;
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
