package com.diyuewang.m.tools.helper;

import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class MapSizeTool {

    /**
     * 计算任意多边形的面积
     * @param latLngLines 经纬度坐标点
     * @return
     */
    public static float calculateArea(List<LatLng> latLngLines) {
        List<double[]> pointFList = new ArrayList<>();
        for (int i = 0; i < latLngLines.size(); i++) {
            LatLng latLng = latLngLines.get(i);
            //经纬度转换成平面直角坐标系
            pointFList.add(WGS2flat(latLng.longitude, latLng.latitude));
        }

        int iCycle, iCount;
        iCycle = 0;
        double iArea = 0;
        iCount = pointFList.size();

        for (iCycle = 0; iCycle < iCount; iCycle++) {
            iArea = iArea + (pointFList.get(iCycle)[0] * pointFList.get((iCycle + 1) % iCount)[1] - pointFList.get((iCycle + 1) % iCount)[0] * pointFList.get(iCycle)[1]);
        }

        return (float) Math.abs(0.5 * iArea);
    }

    // 地球长半轴
    public static final double EARTH_RADIUS = 6378.137;
    /**
     * 经纬度转换成以米为单位的平面直角坐标
     *
     * @param lon 经度
     * @param lat 纬度
     * @return 平面直角坐标double型数组，以米为单位
     */
    public static double[] WGS2flat(double lon, double lat) {
        double L = CalcRad(lon);
        double l = L - CalcRad(120);
        double B = CalcRad(lat);
        double cosb = Math.cos(B);
        double sinb = Math.sin(B);

        double a = EARTH_RADIUS * 1000;
        // 地球短半轴
        double b = 6356752.3142451793;
        double t = Math.tan(B);
        // double r = 3600 * 180 / Math.PI;
        double e2 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(a, 2);
        double e12 = (Math.pow(a, 2) - Math.pow(b, 2)) / Math.pow(b, 2);
        double n2 = e12 * Math.pow(cosb, 2);
        double N = a / Math.sqrt(1 - e2 * Math.pow(sinb, 2));

        double x = 6367449.1458 * B - 32009.8185 * cosb * sinb - 133.9975
                * cosb * Math.pow(sinb, 3) - 0.6975 * cosb * Math.pow(sinb, 5);
        double X = x + N / 2 * t * Math.pow(cosb, 2) * Math.pow(l, 2) + N / 24
                * t * Math.pow(cosb, 4)
                * (5 - Math.pow(t, 2) + 9 * n2 + 4 * Math.pow(n2, 2))
                * Math.pow(l, 4);
        double Y = N * cosb * l + N / 6 * Math.pow(cosb, 3)
                * (1 - Math.pow(t, 2) + n2) * Math.pow(l, 3);

        double[] coord = {X, Y};
        return coord;
    }

    /**
     * 计算弧度
     *
     * @param d 以度为单位的经纬度数值
     * @return 以弧度为单位的经纬度数值
     */
    public static double CalcRad(double d) {
        return d * Math.PI / 180.0;
    }
}
