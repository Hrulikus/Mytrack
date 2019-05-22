import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import static java.awt.Color.RED;
import static java.awt.Color.WHITE;

class PointsPanel extends JPanel {
    Logger logger = Logger.getLogger(this.getClass().getName());
    private Points POINTS = Points.EMPTY;
    private boolean isActive = false;
    private boolean alreadyDrawn = false;
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

    public void updatePointsPanel () {
        logger.info("method called");
        Dimension dimension = POINTS.locatePointsAtPanelAndResizePanel(this);
        setSize((int) dimension.getWidth(), (int) dimension.getHeight());
        //this.paintComponent(this.getGraphics());
    }

    public void updatePointsPanelWithoutPanelResize () {
        logger.info("method called");
        POINTS.locatePointsAtPanel(this);
        //this.repaint();
    }

    public void setSize (int width, int height) {
        double scaleX = getSize().getWidth() / width;
        double scaleY = getSize().getHeight() / height;
        super.setSize(width, height);
        logger.info("method called");
        scalePointsToNewImageSize(scaleX, scaleY);
    }

    private void scalePointsToNewImageSize (double scaleX, double scaleY) {
        if (this.getGraphics() != null && alreadyDrawn) {
            Graphics2D g2d = (Graphics2D) this.getGraphics();
            logger.info("put scaled points(" + scaleX + ";" + scaleY + ")");
            g2d.scale(scaleX, scaleY);
        }
    }

    public void paintComponent (Graphics g) {
        super.paintComponent(g);
        logger.info("method called");
        if (!alreadyDrawn && POINTS.size() > 0) {
            logger.info("already drawn is false and Points.size() > 0");
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(RED);
            g2.setStroke(new BasicStroke(3));
            for (int i = 0; i < POINTS.size() - 1; i++) {
                g2.drawLine((int) POINTS.get(i).getPanelX(), (int) (POINTS.get(i).getPanelY()),
                            (int) POINTS.get(i + 1).getPanelX(), (int) (POINTS.get(i + 1).getPanelY()));
            }
            alreadyDrawn = true;
        }
        else if (alreadyDrawn) {

            this.getGraphics().drawImage(createImageFromPoints(), 0, 0, this);
            scalePointsToNewImageSize(1d, 1d);
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

    public boolean getActiveFlag () {
        logger.info("method called");
        return isActive;
    }

    public Points getPoints () {
        logger.info("method called");
        return POINTS;
    }

    private BufferedImage createImageFromPoints () {
        logger.info("method called");
        BufferedImage image = new BufferedImage(
                this.getWidth(),
                this.getHeight(),
                BufferedImage.TYPE_INT_RGB
        );
        // call the Component's paint method, using
        // the Graphics object of the image.
        //this.paint(image.getGraphics()); // alternately use .printAll(..)
        return image;
    }

    public void movePanel (int x, int y) {
        logger.info("method called");
        java.awt.Point positionBeforeMove = getLocation();
        setLocation((int) positionBeforeMove.getX() + x,
                    (int) positionBeforeMove.getY() + y);
    }
}
