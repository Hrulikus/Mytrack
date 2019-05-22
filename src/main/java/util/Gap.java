package util;

public class Gap {
    public int top;
    public int bottom;
    public int left;
    public int right;

    public Gap (int all, int bottom) {
        top = left = right = all;
        this.bottom = bottom;
    }
}