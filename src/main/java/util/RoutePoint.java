package util;

import java.awt.*;
import java.util.logging.Logger;

import static java.awt.Color.RED;
import static java.util.logging.Level.WARNING;

public class RoutePoint {
    private double panelX;
    private double panelY;
    private double earthX;
    private double earthY;
    private double angleLonInDegrees;
    private double angleLatInDegrees;
    private double angleLonInRadians;
    private double angleLatInRadians;
    public Color pointColor = RED;
    private static final double EARTH_RADIUS = 10000d;
    private static final Logger logger = Logger.getLogger(RoutePoint.class.getName());

    public RoutePoint (double lon, double lat) {
        logger.setLevel(WARNING);
        angleLatInDegrees = lat;
        angleLonInDegrees = lon;
        angleLonInRadians = convertDegreesIntoRadian(lon);
        angleLatInRadians = convertDegreesIntoRadian(lat);
        logger.info("adding new point having radians: " + lon + ";" + lat);
        earthX = convertRadianToEarthX(lon, lat);
        earthY = convertRadianToEarthY(lon, lat);
        logger.info("calculated earth x and y: " + earthX + ";" + earthY);
    }

    public double getAngleLatInDegrees () {
        return angleLatInDegrees;
    }


    public double getAngleLonInDegrees () {
        return angleLonInDegrees;
    }

    public double getAngleLatInRadians () {
        return angleLatInRadians;
    }

    public double getAngleLonInRadians () {
        return angleLonInDegrees;
    }

    private static double convertRadianToEarthX (double lon, double lat) {
        return EARTH_RADIUS * Math.cos(lat) * Math.sin(lon);
    }

    private static double convertRadianToEarthY (double lon, double lat) {
        return EARTH_RADIUS * Math.cos(lat) * Math.cos(lon);
    }

    public double getEarthX () {
        return earthX;
    }

    public double getEarthY () {
        return earthY;
    }

    public void setEarthX (double x) {
        this.earthX = x;
    }

    public void setEarthY (double y) {
        this.earthY = y;
    }

    public static double convertDegreesIntoRadian (double value) {
        return value * Math.PI / 180;
    }

    public double getPanelX () {
        return panelX;
    }

    public double getPanelY () {
        return panelY;
    }

    public void setPanelX (double x) {
        this.panelX = x;
    }

    public void setPanelY (double y) {
        this.panelY = y;
    }
}