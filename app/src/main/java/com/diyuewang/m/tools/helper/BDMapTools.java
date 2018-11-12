package com.diyuewang.m.tools.helper;

import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

import java.util.ArrayList;
import java.util.List;

public class BDMapTools {
  /**
   * 方法说明 :获取周长
   *
   * @author jokerPu
   * 创建时间：2018-11-11
   * <p>@param </p>
   */
  public double getPerimeter(List<Double[]> points) {
    double perimeter = 0;
    if (points != null && !points.isEmpty()) {
      for (int i = 0; i < points.size() - 1; i++) {
        Double[] startPoint = points.get(i);
        Double[] endPoint = points.get(i - 1);
        perimeter = perimeter + DistanceUtil.getDistance(new LatLng(startPoint[0], startPoint[1]), new LatLng(endPoint[0], endPoint[1]));
      }
    }
    return perimeter;
  }

  /**
   * 方法说明 :获取边数
   *
   * @author jokerPu
   * 创建时间：2018-11-11
   * <p>@param </p>
   */
  public int getSides(int points) {
    int sides = 0;
    if (points > 1) {
      sides = points - 1;
    }
    return sides;
  }

  /**
   * 方法说明 :获取多边形面积
   *
   * @author jokerPu
   * 创建时间：2018-11-11
   * <p>@param </p>
   */
  public double getArea(List<Double[]> points) {
    double area = 0;
    //获取边数
    int sides = this.getSides(points.size());
    //计算周长
    double perimeter = this.getPerimeter(points);
    //转化点到LatLng
    List<LatLng> latLngs = new ArrayList<>();
    if(points != null && !points.isEmpty()){
      for (Double[] point : points){
        latLngs.add(new LatLng(point[0], point[1]));
      }
    }else {
      return area;
    }
    area = Double.valueOf(this.getAreaProtected(latLngs));
    return area;
  }

  public static double getTotalArea(List<LatLng> pts) {
    double area = 0;
    //获取边数
//    int sides = this.getSides(points.size());
    //计算周长
//    double perimeter = this.getPerimeter(points);
    //转化点到LatLng
    List<LatLng> latLngs = new ArrayList<>();
    if(pts == null || pts.size() < 3){
      return area;
    }
    area = Double.valueOf(getAreaProtected(latLngs));
    return area;
  }

  /**
   * 计算多边形面积 * @return
   */
  protected static String getAreaProtected(List<LatLng> pts) {
    double totalArea = 0;
    // 初始化总面积
    double LowX = 0.0;
    double LowY = 0.0;
    double MiddleX = 0.0;
    double MiddleY = 0.0;
    double HighX = 0.0;
    double HighY = 0.0;
    double AM = 0.0;
    double BM = 0.0;
    double CM = 0.0;
    double AL = 0.0;
    double BL = 0.0;
    double CL = 0.0;
    double AH = 0.0;
    double BH = 0.0;
    double CH = 0.0;
    double CoefficientL = 0.0;
    double CoefficientH = 0.0;
    double ALtangent = 0.0;
    double BLtangent = 0.0;
    double CLtangent = 0.0;
    double AHtangent = 0.0;
    double BHtangent = 0.0;
    double CHtangent = 0.0;
    double ANormalLine = 0.0;
    double BNormalLine = 0.0;
    double CNormalLine = 0.0;
    double OrientationValue = 0.0;
    double AngleCos = 0.0;
    double Sum1 = 0.0;
    double Sum2 = 0.0;
    double Count2 = 0;
    double Count1 = 0;
    double Sum = 0.0;
    double Radius = 6378137.0;
    // WGS84椭球半径
    int Count = pts.size();
    // 最少3个点
    if (Count < 3) {
      return null;
    }
    for (int i = 0; i < Count; i++) {
      if (i == 0) {
        LowX = pts.get(Count - 1).longitude * Math.PI / 180;
        LowY = pts.get(Count - 1).latitude * Math.PI / 180;
        MiddleX = pts.get(0).longitude * Math.PI / 180;
        MiddleY = pts.get(0).latitude * Math.PI / 180;
        HighX = pts.get(1).longitude * Math.PI / 180;
        HighY = pts.get(1).latitude * Math.PI / 180;
      } else if (i == Count - 1) {
        LowX = pts.get(Count - 2).longitude * Math.PI / 180;
        LowY = pts.get(Count - 2).latitude * Math.PI / 180;
        MiddleX = pts.get(Count - 1).longitude * Math.PI / 180;
        MiddleY = pts.get(Count - 1).latitude * Math.PI / 180;
        HighX = pts.get(0).longitude * Math.PI / 180;
        HighY = pts.get(0).latitude * Math.PI / 180;
      } else {
        LowX = pts.get(i - 1).longitude * Math.PI / 180;
        LowY = pts.get(i - 1).latitude * Math.PI / 180;
        MiddleX = pts.get(i).longitude * Math.PI / 180;
        MiddleY = pts.get(i).latitude * Math.PI / 180;
        HighX = pts.get(i + 1).longitude * Math.PI / 180;
        HighY = pts.get(i + 1).latitude * Math.PI / 180;
      }
      AM = Math.cos(MiddleY) * Math.cos(MiddleX);
      BM = Math.cos(MiddleY) * Math.sin(MiddleX);
      CM = Math.sin(MiddleY);
      AL = Math.cos(LowY) * Math.cos(LowX);
      BL = Math.cos(LowY) * Math.sin(LowX);
      CL = Math.sin(LowY);
      AH = Math.cos(HighY) * Math.cos(HighX);
      BH = Math.cos(HighY) * Math.sin(HighX);
      CH = Math.sin(HighY);
      CoefficientL = (AM * AM + BM * BM + CM * CM) / (AM * AL + BM * BL + CM * CL);
      CoefficientH = (AM * AM + BM * BM + CM * CM) / (AM * AH + BM * BH + CM * CH);
      ALtangent = CoefficientL * AL - AM;
      BLtangent = CoefficientL * BL - BM;
      CLtangent = CoefficientL * CL - CM;
      AHtangent = CoefficientH * AH - AM;
      BHtangent = CoefficientH * BH - BM;
      CHtangent = CoefficientH * CH - CM;
      AngleCos = (AHtangent * ALtangent + BHtangent * BLtangent + CHtangent * CLtangent) / (Math.sqrt(AHtangent * AHtangent + BHtangent * BHtangent + CHtangent * CHtangent) * Math.sqrt(ALtangent * ALtangent + BLtangent * BLtangent + CLtangent * CLtangent));
      AngleCos = Math.acos(AngleCos);
      ANormalLine = BHtangent * CLtangent - CHtangent * BLtangent;
      BNormalLine = 0 - (AHtangent * CLtangent - CHtangent * ALtangent);
      CNormalLine = AHtangent * BLtangent - BHtangent * ALtangent;
      if (AM != 0) OrientationValue = ANormalLine / AM;
      else if (BM != 0) OrientationValue = BNormalLine / BM;
      else OrientationValue = CNormalLine / CM;
      if (OrientationValue > 0) {
        Sum1 += AngleCos;
        Count1++;
      } else {
        Sum2 += AngleCos;
        Count2++;
      }
    }
    double tempSum1, tempSum2;
    tempSum1 = Sum1 + (2 * Math.PI * Count2 - Sum2);
    tempSum2 = (2 * Math.PI * Count1 - Sum1) + Sum2;
    if (Sum1 > Sum2) {
      if ((tempSum1 - (Count - 2) * Math.PI) < 1) Sum = tempSum1;
      else Sum = tempSum2;
    } else {
      if ((tempSum2 - (Count - 2) * Math.PI) < 1) Sum = tempSum2;
      else Sum = tempSum1;
    }
    totalArea = (Sum - (Count - 2) * Math.PI) * Radius * Radius;
    return String.valueOf(Math.floor(totalArea)); // 返回总面积
  }
}
