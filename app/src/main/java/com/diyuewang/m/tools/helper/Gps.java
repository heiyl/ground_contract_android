package com.diyuewang.m.tools.helper;

public class Gps {
    private double wgLat;
    private double wgLon;

    public Gps() {
    }

    public Gps(double wgLat, double wgLon) {
        this.wgLat = wgLat;
        this.wgLon = wgLon;
    }

    public double getWgLat() {
        return wgLat;
    }

    public void setWgLat(double wgLat) {
        this.wgLat = wgLat;
    }

    public double getWgLon() {
        return wgLon;
    }

    public void setWgLon(double wgLon) {
        this.wgLon = wgLon;
    }
}
