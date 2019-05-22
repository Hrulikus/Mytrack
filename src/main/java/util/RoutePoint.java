package util;

public class RoutePoint {
    private double coordinateX;
    private double coordinateY;
    private double panelX;
    private double panelY;

    public RoutePoint (double x, double y) {
        coordinateX = x;
        coordinateY = y;
    }

    public double getX () {
        return coordinateX;
    }

    public double getY () {
        return coordinateY;
    }

    public void setX (double x) {
        this.coordinateX = x;
    }

    public void setY (double y) {
        this.coordinateY = y;
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