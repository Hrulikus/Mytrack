import util.Gap;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import static java.awt.Color.WHITE;

class DrawingPanel extends JPanel {
    private static final Logger logger = Logger.getLogger(DrawingPanel.class.getName());

    private BufferedImage myPicture = null;
    private Gap gap;
    private PointsPanel pointsPanel;

    DrawingPanel (int width, int height) {
        logger.info("method called");
        Dimension preferredPanelSize = calculatePreferredSize(width, height);
        setPanelLocationAndSize(preferredPanelSize);
        setBackground(WHITE);
        setLayout(null);
        addPointsPanel();
        setVisible(true);
    }

    private void addPointsPanel () {
        logger.info("method called");
        pointsPanel = new PointsPanel();
        add(pointsPanel);
    }

    public PointsPanel getPointsPanel () {
        logger.info("method called");
        return pointsPanel;
    }

    public Gap getGap () {
        logger.info("method called");
        return gap;
    }

    private void setPanelLocationAndSize (Dimension d) {
        logger.info("method called");
        int panelWidth = (int) d.getWidth();
        int panelHeight = (int) d.getHeight();
        logger.info("Panel with Location: " + gap.left + " and height: " + gap.top);
        logger.info("Panel with width: " + panelWidth + " and height: " + panelHeight);
        setLocation(gap.left, gap.top);
        setSize(d);
    }

    private Dimension calculatePreferredSize (int width, int height) {
        logger.info("method called");
        int gapBasis = 10;
        gap = new Gap(gapBasis, gapBasis * 5);
        int panelWidth = width - gapBasis * 2;
        int panelHeight = height - gapBasis * 6;
        return new Dimension(panelWidth, panelHeight);
    }

    void drawScaledImageAtPanel (BufferedImage image) {
        logger.info("method called");
        logger.info("Starting printing Buffered image to Panel:");
        logger.info("Panel size: " + this.getWidth() + " " + this.getHeight());
        logger.info("Image size: " + image.getWidth() + " " + image.getHeight());
        myPicture = image;
        paintComponent(this.getGraphics());
    }

    public void paintComponent (Graphics g) {
        logger.info("method called");
        super.paintComponent(g);
        g.drawImage(myPicture, 0, 0, this);
    }
}
