import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
}