import util.Gap;
import util.ImageUtil;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import static java.lang.System.exit;

class Main extends JFrame {
    //TODO: make images resizable
    //TODO: make images rotatable
    //TODO: add saver to file
    //TODO: add image editor
    //TODO: add log4j
    //TODO: add Layout to Frame
    //TODO: migrate to swing? swt? javafx?
    //TODO: check https://www.baeldung.com/java-images for better approach in images
    //TODO: add resizable for frame
    //TODO: add image preview to loader


    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private DrawingPanel drawingPanel;
    private ButtonsPanel buttonsPanel;

    private static final Dimension MAX_FRAME_SIZE = new Dimension(1380, 740);

    public static void main (String... args) {
        try {
            new Main();
        } catch (Exception globalException) {
            globalException.printStackTrace();
        }
    }

    private Main () {
        setSize(500, 300);
        setPreferredSize(new Dimension(500, 300));
        setLayout(null);
        pack();
        addWindowListener(new WindowAdapter() {
            public void windowClosing (WindowEvent e) {
                exit(0);
            }
        });
        addObjectsToFrame();
        setResizable(false);
        setVisible(true);
    }

    public Dimension getMaxFrameSize () {
        return MAX_FRAME_SIZE;
    }

    private void addObjectsToFrame () {
        addDrawingPanel();
        addButtonsPanel();
    }

    private void addButtonsPanel () {
        buttonsPanel = new ButtonsPanel(this);
        buttonsPanel.setVisible(true);
        add(buttonsPanel);
    }

    private void addDrawingPanel () {
        Container contentPane = this.getContentPane();
        drawingPanel = new DrawingPanel(contentPane.getWidth(), contentPane.getHeight());
        getContentPane().add(drawingPanel);
    }

    public DrawingPanel getDrawingPanel () {
        return drawingPanel;
    }


    public void resizeFrameAndRepaintImage (BufferedImage image) {
        setVisible(false);

        Dimension maxFrameSize = getMaxFrameSize();
        boolean shouldImageBeScaled = false;
        BufferedImage newImage;
        Dimension newImageSize;

        Dimension newFrameSize = getFrameSizeFromPanelSize(image.getWidth(), image.getHeight());
        logger.info("New calculated Frame Size: " + newFrameSize.getWidth() + " " + newFrameSize.getHeight());

        if (firstSizeNotFitSecond(newFrameSize, maxFrameSize)) {
            newFrameSize = calculateNewFrameSize(image);
            logger.warning("Frame size is over maximum one, so will be overriden");
            shouldImageBeScaled = true;
        }


        logger.info("Setting new JFrame size to " + newFrameSize.width + " " + newFrameSize.height);
        setSize(newFrameSize);
        if (shouldImageBeScaled) {
            newImageSize = calculateNewImageSize(newFrameSize);
            newImage = ImageUtil.scaleImage(image, newImageSize);
            drawingPanel.setSize(newImageSize.width, newImageSize.height);
        }
        else {
            newImage = image;
        }

        drawingPanel.drawScaledImageAtPanel(newImage);

        buttonsPanel.resizeAndRelocatePanel(newFrameSize);
        buttonsPanel.updateSizeLabel(newImage.getWidth(), newImage.getHeight());

        setVisible(true);
    }

    private Dimension calculateNewFrameSize (BufferedImage image) {

        Dimension maxFrameSize = getMaxFrameSize();
        Dimension maxPanelSize = getMaxPanelSize(maxFrameSize);

        double scaleByX = maxPanelSize.getWidth() / image.getWidth();
        double scaleByY = maxPanelSize.getHeight() / image.getHeight();

        if (image.getHeight() * scaleByX < maxPanelSize.getHeight())
            return getFrameSizeFromPanelSize((int) maxPanelSize.getWidth(), (int) (image.getHeight() * scaleByX));
        else if (image.getWidth() * scaleByY < maxPanelSize.getWidth())
            return getFrameSizeFromPanelSize((int) (image.getWidth() * scaleByY), (int) maxPanelSize.getHeight());
        else
            throw new RuntimeException("Unable to scale image correctly");

    }

    private Dimension getMaxPanelSize (Dimension maxFrameSize) {
        Gap gap = getDrawingPanel().getGap();
        Insets insets = getInsets();
        return new Dimension((int) (maxFrameSize.getWidth() - gap.left - gap.right - insets.left - insets.right),
                             (int) (maxFrameSize.getHeight() - gap.top - gap.bottom - insets.top - insets.bottom));
    }

    boolean firstSizeNotFitSecond (Dimension size1, Dimension size2) {
        return size1.getHeight() > size2.getHeight() || size1.getWidth() > size1.getWidth();
    }

    private Dimension calculateNewImageSize (Dimension newFrameSize) {
        Gap gap = getDrawingPanel().getGap();
        Insets insets = getDrawingPanel().getInsets();
        return new Dimension((int) (newFrameSize.getWidth() - (gap.right + gap.left + insets.left + insets.right)),
                             (int) (newFrameSize.getHeight() - (gap.top + gap.bottom + insets.top + insets.bottom)));
    }

    private Dimension getFrameSizeFromPanelSize (int imageWidth, int imageHeight) {
        Gap gap = getDrawingPanel().getGap();
        Insets insets = getInsets();
        return new Dimension(imageWidth + gap.left + gap.right + insets.left + insets.right,
                             imageHeight + gap.top + gap.bottom + insets.top + insets.bottom);
    }
}