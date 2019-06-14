import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
    private static final Label SIZE_LABEL = new Label();

    private Main parentFrame;

    ButtonsPanel (Main parentFrame) {
        super();
        this.parentFrame = parentFrame;
        addLoadImageButton();
        addLoadGpxButton();
        addImageSizeLabel();
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
        SIZE_LABEL.setPreferredSize(new Dimension(this.getWidth() / 5, 30));
    }

    private void addImageSizeLabel () {
        SIZE_LABEL.setVisible(true);
        add(SIZE_LABEL);
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

    public void updateSizeLabel (int width, int height) {
        SIZE_LABEL.setText("Drawing Panel size: " + width + ";" + height);
    }

    private void loadFileAndDrawAtPanel () {
        logger.info("Starting loading image from file");
        File file = loadUsingFileChooser(new FileNameExtensionFilter("Images", "jpg"));
        if (file == null) {
            logger.warning("user has not selected any file");
            return;
        }
        BufferedImage bufferedImage = convertFileToBufferedImage(file);
        parentFrame.resizeFrameAndRepaintImage(bufferedImage);
    }

    public void locatePanel (Dimension newFrameSize) {
        int buttonsY = newFrameSize.height - parentFrame.getInsets().bottom - parentFrame.getDrawingPanel().getGap().bottom;
        logger.info("Setting new buttons panel location: 0 " + buttonsY);
        this.setLocation(0, buttonsY);
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
        //TODO: find new approach from https://stackoverflow.com/questions/3495052/gpx-parser-for-java
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
                    double pointLon = Double.parseDouble(attributeLon);
                    double pointLat = Double.parseDouble(attributeLat);

                    points.add(pointLon, pointLat);
                }
            }
            pointsPanel.locatePointsForFirstTime(drawingPanel);
            pointsPanel.repaint();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void resizeAndRelocatePanel (Dimension frameSize) {
        setSize((int) frameSize.getWidth(), 40);
        locatePanel(frameSize);
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
