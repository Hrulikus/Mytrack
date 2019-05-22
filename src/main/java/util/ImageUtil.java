package util;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

public class ImageUtil {
    private static final Logger logger = Logger.getLogger(ImageUtil.class.getName());
    public static BufferedImage scaleImage (BufferedImage image, Dimension dimension) {
        int w = image.getWidth();
        int h = image.getHeight();
        logger.info("Scaling image with width and height: " + w + " " + h);
        double panelWidth = dimension.getWidth();
        double panelHeight = dimension.getHeight();
        logger.info("Panel with width and height: " + panelWidth + " " + panelHeight);
        double scale_x = panelWidth / w;
        double scale_y = panelHeight / h;
        logger.info("Resulting scale: " + scale_x + " " + scale_y);
        BufferedImage after = new BufferedImage((int) panelWidth, (int) panelHeight, BufferedImage.TYPE_INT_ARGB);
        AffineTransform at = new AffineTransform();
        at.scale(scale_x, scale_y);
        AffineTransformOp scaleOp = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
        after = scaleOp.filter(image, after);
        return after;
    }
}
