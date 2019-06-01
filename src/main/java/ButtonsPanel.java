import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import util.Gap;
import util.ImageUtil;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Logger;

class ButtonsPanel extends JPanel {
    private static final Logger logger = Logger.getLogger(ButtonsPanel.class.getName());

    private static final String USER_HOME = System.getProperty("user.home");
    private static final String IDEA_RESOURCE_DIR = "/IdeaProjects/Maps/src/main/resources/";

    private static final LoadButton LOAD_IMAGE_BUTTON = new LoadButton("Load image");
    private static final LoadButton LOAD_GPX_BUTTON = new LoadButton("Load GPX file");

    private Main parentFrame;

    ButtonsPanel (Main parentFrame) {
        super();
        this.parentFrame = parentFrame;
        addLoadImageButton();
        addLoadGpxButton();
        setSize(parentFrame.getContentPane().getWidth() - 20, 40);
        setLocation(0, parentFrame.getContentPane().getHeight() - 50);
    }

    public void setSize (int width, int height) {
        super.setSize(width, height);
        resizeAndRelocateButtons();
    }

    private void resizeAndRelocateButtons () {
        LOAD_IMAGE_BUTTON.setPreferredSize(new Dimension(this.getWidth() / 5, 30));
        LOAD_GPX_BUTTON.setPreferredSize(new Dimension(this.getWidth() / 5, 30));
    }

    private void addLoadImageButton () {
        LOAD_IMAGE_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {
                loadFileAndDrawAtPanel();
            }
        });
        LOAD_IMAGE_BUTTON.setVisible(true);
        add(LOAD_IMAGE_BUTTON);
    }

    private void addLoadGpxButton () {
        LOAD_GPX_BUTTON.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed (ActionEvent e) {
                loadGpxFileAndDrawAtPanel();
            }
        });
        LOAD_GPX_BUTTON.setVisible(true);
        add(LOAD_GPX_BUTTON);
    }

    private void loadFileAndDrawAtPanel () {
        logger.info("Starting loading image from file");
        File file = loadUsingFileChooser(new FileNameExtensionFilter("Images", "jpg"));
        if (file == null) {
            logger.warning("user has not selected any file");
            return;
        }
        BufferedImage bufferedImage = convertFileToBufferedImage(file);
        resizeFrameAndRepaintImage(bufferedImage);
    }


    private void resizeFrameAndRepaintImage (BufferedImage image) {
        parentFrame.setVisible(false);
        Dimension maxFrameSize = parentFrame.getMaxFrameSize();
        boolean shouldImageBeScaled = false;
        BufferedImage newImage;
        Dimension newImageSize;

        Dimension newFrameSize = getNewFrameSize(image);

        logger.info("New calculated Frame Size: " + newFrameSize.getWidth() + " " + newFrameSize.getHeight());
        if (newFrameSize.getWidth() > maxFrameSize.getWidth() ||
                    newFrameSize.getHeight() > maxFrameSize.getHeight()) {
            newFrameSize = maxFrameSize;
            logger.warning("Frame size is over maximum one, so will be overriden");
            shouldImageBeScaled = true;
        }


        logger.info("Setting new JFrame size to " + newFrameSize.width + " " + newFrameSize.height);
        parentFrame.setSize(newFrameSize);
        if (shouldImageBeScaled) {
            newImageSize = calculateImageSizeFromFrame(newFrameSize);
            newImage = ImageUtil.scaleImage(image, newImageSize);
            parentFrame.getDrawingPanel().setSize(newImageSize.width, newImageSize.height);
        }
        else {
            newImage = image;
        }

        parentFrame.getDrawingPanel().drawScaledImageAtPanel(newImage);

        resizeAndRelocatePanel(newFrameSize);
        parentFrame.setVisible(true);
    }

    private void resizeAndRelocatePanel (Dimension frameSize) {
        setSize((int) frameSize.getWidth(), 40);
        locatePanel(frameSize);
    }

    private Dimension calculateImageSizeFromFrame (Dimension newFrameSize) {
        Gap gap = parentFrame.getDrawingPanel().getGap();
        Insets insets = parentFrame.getDrawingPanel().getInsets();
        return new Dimension((int) (newFrameSize.getWidth() - (gap.right + gap.left + insets.left + insets.right)),
                             (int) (newFrameSize.getHeight() - (gap.top + gap.bottom + insets.top + insets.bottom)));
    }

    private void locatePanel (Dimension newFrameSize) {
        int buttonsY = newFrameSize.height - parentFrame.getInsets().bottom - parentFrame.getDrawingPanel().getGap().bottom;
        logger.info("Setting new buttons panel location: 0 " + buttonsY);
        this.setLocation(0, buttonsY);
    }

    private Dimension getNewFrameSize (BufferedImage image) {
        int imageWidth = image.getWidth();
        int imageHeight = image.getHeight();
        Gap gap = parentFrame.getDrawingPanel().getGap();
        Insets insets = parentFrame.getInsets();
        return new Dimension(imageWidth + gap.left + gap.right + insets.left + insets.right,
                             imageHeight + gap.top + gap.bottom + insets.top + insets.bottom);
    }

    private void loadGpxFileAndDrawAtPanel () {
        logger.info("Starting loading image from file");
        File file = loadUsingFileChooser(new FileNameExtensionFilter("gpx", "gpx"));
        if (file == null) {
            logger.warning("user has not selected any GPX file");
            return;
        }
        parseAndDrawPoints(file);
    }

    private File loadUsingFileChooser (FileNameExtensionFilter filter) {
        JFileChooser fc = new JFileChooser(USER_HOME + IDEA_RESOURCE_DIR);
        if (filter != null)
            fc.setFileFilter(filter);
        int result = fc.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return fc.getSelectedFile();
        }
        return null;
    }


    private void parseAndDrawPoints (File file) {
        try {
            DrawingPanel drawingPanel = parentFrame.getDrawingPanel();
            PointsPanel pointsPanel = drawingPanel.getPointsPanel();
            Points points = pointsPanel.getPoints();

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            doc.getDocumentElement().normalize();


            Element root = doc.getDocumentElement();

            Element trk = (Element) root.getElementsByTagName("trk").item(0);

            Element trkSeg = (Element) trk.getElementsByTagName("trkseg").item(0);

            NodeList pointsList = trkSeg.getElementsByTagName("trkpt");
            logger.info("Starting parsing POINTS from route:");

            for (int temp = 0; temp < pointsList.getLength(); temp++) {
                Node node = pointsList.item(temp);
                Element element = (Element) node;
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    String attributeLat = element.getAttribute("lat");
                    String attributeLon = element.getAttribute("lon");
                    double coordinateX = Double.parseDouble(attributeLon);
                    double coordinateY = Double.parseDouble(attributeLat);

                    points.add(coordinateX, coordinateY);
                }
            }
            pointsPanel.locatePointsForFirstTime(drawingPanel);
            pointsPanel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private BufferedImage convertFileToBufferedImage (File file) {
        String fileName = file.getName();
        logger.info("Input file with filename " + fileName);
        InputStream imagePath = getClass().getResourceAsStream(fileName);
        BufferedImage bufferedImage;
        try {
            bufferedImage = ImageIO.read(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return bufferedImage;
    }
}
