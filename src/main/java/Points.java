import util.RoutePoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import static java.awt.Color.GREEN;
import static java.util.logging.Level.INFO;

class Points extends ArrayList<RoutePoint> {
    private static final Logger logger = Logger.getLogger(Points.class.getName());

    private List<RoutePoint> points;

    private RoutePoint minimum;
    private RoutePoint maximum;

    static Points EMPTY = new Points();

    private Points () {
        logger.setLevel(INFO);
        points = new ArrayList<>();
    }

    void add (double lon, double lat) {
        logger.info("adding new point having degrees: " + lon + ";" + lat);
        RoutePoint newPoint = new RoutePoint(lon, lat);
        checkAndUpdateBounds(newPoint);
        points.add(newPoint);
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

    private void checkAndUpdateBounds (RoutePoint point) {
        double earthX = point.getEarthX();
        double earthY = point.getEarthY();

        if (minimum == null) {
            minimum = new RoutePoint(0d, 0d);
            minimum.setEarthX(earthX);
            minimum.setEarthY(earthY);
        }
        if (maximum == null) {
            maximum = new RoutePoint(0d, 0d);
            maximum.setEarthX(earthX);
            maximum.setEarthY(earthY);
        }

        logger.info("checking point: " + earthX + ";" + earthY);
        if (earthX < minimum.getEarthX()) {
            minimum.setEarthX(earthX);
            point.pointColor = GREEN;
        }
        if (earthX > maximum.getEarthX()) {
            maximum.setEarthX(earthX);
            point.pointColor = GREEN;
        }
        if (earthY < minimum.getEarthY()) {
            minimum.setEarthY(earthY);
            point.pointColor = GREEN;
        }
        if (earthY > maximum.getEarthY()) {
            maximum.setEarthY(earthY);
            point.pointColor = GREEN;
        }
    }


    public void locatePointsAtPanel (PointsPanel panel) {
        logger.info("method called with PointsPanel par and panel has size: " + panel.getWidth() + ";" + panel.getHeight());

        locatePointsAtPanel2(panel.getSize());
    }

    public void locatePointsAtPanel (Dimension panelSize) {
        logger.info("method called with Dimension par has size: " + panelSize.getWidth() + ";" + panelSize.getHeight());

        Dimension drawBounds = new Dimension((int) panelSize.getWidth() - 2,
                                             (int) panelSize.getHeight() - 2);

        double maximumX = getMaximum().getEarthX();
        double minimumX = getMinimum().getEarthX();
        double maximumY = getMaximum().getEarthY();
        double minimumY = getMinimum().getEarthY();

        logger.info("Minimum x and y: " + minimumX + ";" + minimumY);
        logger.info("Maximum x and y: " + maximumX + ";" + maximumY);

        double spreadX = maximumX - minimumX;
        double spreadY = maximumY - minimumY;

        logger.info("Spread x and y: " + spreadX + ";" + spreadY);

        double koefX = drawBounds.getWidth() / spreadX;
        double koefY = drawBounds.getHeight() / spreadY;

        double minKoef = Math.min(koefX, koefY);

        for (RoutePoint currentPoint : points) {

            double x = (currentPoint.getEarthX() - minimumX) * minKoef + 1;
            double y = (currentPoint.getEarthY() - minimumY) * minKoef + 1;

            logger.info("Calculated x and y: " + x + ";" + y);
            currentPoint.setPanelX(x);
            currentPoint.setPanelY(y);
        }
    }

    public void locatePointsAtPanel2 (Dimension panelSize) {
        logger.info("method called with Dimension par has size: " + panelSize.getWidth() + ";" + panelSize.getHeight());

        double dLon;
        double dLat;

        double a = 6378245.0;
        double b = 6356863.019;

        double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
        double n = (a - b) / (a + b);

        double F = 1.0;
        double Lat0 = 0.0;

        double N0 = 0.0;

        double minX = 7777777d;
        double maxX = 0d;
        double minY = 7777777d;
        double maxY = 0d;

        for (RoutePoint currentPoint : points) {
            dLon = currentPoint.getAngleLonInDegrees();
            dLat = currentPoint.getAngleLatInDegrees();

            int zone = (int) (dLon / 6.0 + 1);

            double Lon0 = (zone * 6 - 3) * Math.PI / 180;
            double E0 = zone * 1e6 + 500000.0;

            double Lat = dLat * Math.PI / 180.0;
            double Lon = dLon * Math.PI / 180.0;

            double sinLat = Math.sin(Lat);
            double cosLat = Math.cos(Lat);
            double tanLat = Math.tan(Lat);

            double v = a * F * Math.pow(1 - e2 * Math.pow(sinLat, 2), -0.5);
            double p = a * F * (1 - e2) * Math.pow(1 - e2 * Math.pow(sinLat, 2), -1.5);
            double n2 = v / p - 1;
            double M1 = (1 + n + 5.0 / 4.0 * Math.pow(n, 2) + 5.0 / 4.0 * Math.pow(n, 3)) * (Lat - Lat0);
            double M2 = (3 * n + 3 * Math.pow(n, 2) + 21.0 / 8.0 * Math.pow(n, 3)) * Math.sin(Lat - Lat0) * Math.cos(Lat + Lat0);
            double M3 = (15.0 / 8.0 * Math.pow(n, 2) + 15.0 / 8.0 * Math.pow(n, 3)) * Math.sin(2 * (Lat - Lat0)) * Math.cos(2 * (Lat + Lat0));
            double M4 = 35.0 / 24.0 * Math.pow(n, 3) * Math.sin(3 * (Lat - Lat0)) * Math.cos(3 * (Lat + Lat0));
            double M = b * F * (M1 - M2 + M3 - M4);
            double I = M + N0;
            double II = v / 2 * sinLat * cosLat;
            double III = v / 24 * sinLat * Math.pow(cosLat, 3) * (5 - Math.pow(tanLat, 2) + 9 * n2);
            double IIIA = v / 720 * sinLat * Math.pow(cosLat, 5) * (61 - 58 * Math.pow(tanLat, 2) + Math.pow(tanLat, 4));
            double IV = v * cosLat;
            double V = v / 6 * Math.pow(cosLat, 3) * (v / p - Math.pow(tanLat, 2));
            double VI = v / 120 * Math.pow(cosLat, 5) * (5 - 18 * Math.pow(tanLat, 2) + Math.pow(tanLat, 4) + 14 * n2 - 58 * Math.pow(tanLat, 2) * n2);

            double N = I + II * Math.pow(Lon - Lon0, 2) + III * Math.pow(Lon - Lon0, 4) + IIIA * Math.pow(Lon - Lon0, 6);
            double E = E0 + IV * (Lon - Lon0) + V * Math.pow(Lon - Lon0, 3) + VI * Math.pow(Lon - Lon0, 5);


            logger.info("Calculated earth x and y: " + N + ";" + E);
            if (N < minY)
                minY = N;
            if (N > maxY)
                maxY = N;
            if (E < minX)
                minX = E;
            if (E > maxX)
                maxX = E;

            currentPoint.setEarthX(E);
            currentPoint.setEarthY(N);
        }


        Dimension drawBounds = new Dimension((int) panelSize.getWidth() - 2,
                                             (int) panelSize.getHeight() - 2);

        double panelX, panelY;

        double spreadX = maxX - minX;
        double spreadY = maxY - minY;

        logger.info("Spread x and y: " + spreadX + ";" + spreadY);

        double koefX = drawBounds.getWidth() / spreadX;
        double koefY = drawBounds.getHeight() / spreadY;

        double minKoef = Math.min(koefX, koefY);
        for (RoutePoint currentPoint : points) {
            panelX = (currentPoint.getEarthX() - minX) * minKoef + 1;
            panelY = (currentPoint.getEarthY() - minY) * minKoef + 1;
            logger.info("Calculated panel x and y: " + panelX + ";" + panelY);
            currentPoint.setPanelX(panelX);
            currentPoint.setPanelY(drawBounds.getHeight() - panelY);
        }
    }
}