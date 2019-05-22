import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

class LoadButton extends JButton {
    private static final Logger logger = Logger.getLogger(LoadButton.class.getName());

    LoadButton (String label) {
        super(label);
    }

    void setSize (Container parentContainer) {
        this.setSize(calculateButtonPrefferedSizeDimension(parentContainer));
    }

    private static Dimension calculateButtonPrefferedSizeDimension (Container parentContainer) {
        Dimension d = new Dimension(parentContainer.getWidth() / 6, 40);
        logger.info("calculated button size: " + d.getWidth() + " " + d.getHeight());
        return d;
    }
}
