import util.RoutePoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

class Points extends ArrayList<RoutePoint> {
    private static final Logger logger = Logger.getLogger(Points.class.getName());

    private List<RoutePoint> points;

    private RoutePoint minimum = new RoutePoint(100, 100);
    private RoutePoint maximum = new RoutePoint(0, 0);

    private Point minimumPanel = new Point(5000, 5000);
    private Point maximumPanel = new Point(0, 0);

    static Points EMPTY = new Points();

    private Points () {
        points = new ArrayList<>();
    }

    void add (double x, double y) {
        checkAndUpdateBounds(x, y);
        points.add(new RoutePoint(x, y));
    }

    @Override
    public Iterator<RoutePoint> iterator () {
        return points.iterator();
    }

    public RoutePoint get (int i) {
        return points.get(i);
    }

    public int size () {
        return points.size();
    }

    public RoutePoint getMinimum () {
        return minimum;
    }

    public RoutePoint getMaximum () {
        return maximum;
    }

    private void checkAndUpdateBounds (double coordinateX, double coordinateY) {
        if (coordinateX < minimum.getX())
            minimum.setX(coordinateX);
        if (coordinateX > maximum.getX())
            maximum.setX(coordinateX);
        if (coordinateY < minimum.getY())
            minimum.setY(coordinateY);
        if (coordinateY > maximum.getY())
            maximum.setY(coordinateY);
    }

    private void checkAndUpdatePanelBounds (double coordinateX, double coordinateY) {
        if (coordinateX < minimumPanel.getX())
            minimumPanel.x = (int) coordinateX;
        if (coordinateX > maximumPanel.getX())
            maximumPanel.x = (int) coordinateX;
        if (coordinateY < minimumPanel.getY())
            minimumPanel.y = (int) coordinateY;
        if (coordinateY > maximumPanel.getY())
            maximumPanel.y = (int) coordinateY;
    }

    Dimension locatePointsAtPanelAndResizePanel (PointsPanel panel) {
        logger.info("Initial PointsPanel size before resize:" + panel.getWidth() + " " + panel.getHeight());

        Dimension panelSize = panel.getSize();
        Dimension drawBounds = new Dimension((int) panelSize.getWidth() - 2,
                                             (int) panelSize.getHeight() - 2);

        double maximumX = getMaximum().getX();
        double minimumX = getMinimum().getX();
        double maximumY = getMaximum().getY();
        double minimumY = getMinimum().getY();

        double spreadX = maximumX - minimumX;
        double spreadY = maximumY - minimumY;

        double koefX = drawBounds.getWidth() / spreadX;
        double koefY = drawBounds.getHeight() / spreadY;

        double minKoef = Math.min(koefX, koefY);

        for (RoutePoint currentPoint : points) {
            double x = (currentPoint.getX() - minimumX) * minKoef + 1;
            double y = (currentPoint.getY() - minimumY) * minKoef + 1;
            currentPoint.setPanelX(x);
            currentPoint.setPanelY(y);
            checkAndUpdatePanelBounds(x, y);
        }

        int newWidth = maximumPanel.x - minimumPanel.x + 2;
        int newHeight = maximumPanel.y - minimumPanel.y + 2;

        logger.info("New PointsPanel size after points recalculate: " + newWidth + " " + newHeight);

        setPointsLocationAtPanel(newHeight);

        return new Dimension(newWidth, newHeight);
    }

    public void locatePointsAtPanel (PointsPanel panel) {
        logger.info("Recalculate points to print at panel with size: " + panel.getWidth() + ";" + panel.getHeight());

        Dimension panelSize = panel.getSize();
        Dimension drawBounds = new Dimension((int) panelSize.getWidth() - 2,
                                             (int) panelSize.getHeight() - 2);

        double maximumX = getMaximum().getX();
        double minimumX = getMinimum().getX();
        double maximumY = getMaximum().getY();
        double minimumY = getMinimum().getY();

        double spreadX = maximumX - minimumX;
        double spreadY = maximumY - minimumY;

        double koefX = drawBounds.getWidth() / spreadX;
        double koefY = drawBounds.getHeight() / spreadY;

        double minKoef = Math.min(koefX, koefY);

        for (RoutePoint currentPoint : points) {
            double x = (currentPoint.getX() - minimumX) * minKoef + 1;
            double y = (currentPoint.getY() - minimumY) * minKoef + 1;
            currentPoint.setPanelX(x);
            currentPoint.setPanelY(drawBounds.getHeight() - y);
        }
    }

    private void setPointsLocationAtPanel (int newHeight) {
        for (RoutePoint currentPoint : points) {
            double x = currentPoint.getPanelX();
            double y = currentPoint.getPanelY();
            currentPoint.setPanelX(x);
            currentPoint.setPanelY(newHeight - y);
        }
    }
}