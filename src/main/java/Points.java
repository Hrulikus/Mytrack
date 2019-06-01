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


    public void locatePointsAtPanel (PointsPanel panel) {
        logger.info("method called with PointsPanel par and panel has size: " + panel.getWidth() + ";" + panel.getHeight());

        locatePointsAtPanel(panel.getSize());
    }

    public void locatePointsAtPanel (Dimension panelSize) {
        logger.info("method called with Dimension par has size: " + panelSize.getWidth() + ";" + panelSize.getHeight());

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

        //double minKoef = Math.min(koefX, koefY);

        for (RoutePoint currentPoint : points) {
            double x = (currentPoint.getX() - minimumX) * koefX + 1;
            double y = (currentPoint.getY() - minimumY) * koefY + 1;
            currentPoint.setPanelX(x);
            currentPoint.setPanelY(drawBounds.getHeight() - y);
        }
    }
}