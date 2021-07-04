package com.example.forestfires.algorithm;

import org.apache.commons.math3.util.FastMath;

/**
 * @author zhangduo
 * @date: 2021/7/4 22:16
 */
public class DistanceCal {
    private static final double R = 6367000.0;

    public static double distanceSimplify(double lat1, double lng1, double lat2, double lng2) {
        // 经度差值
        double dx = lng1 - lng2;
        // 纬度差值
        double dy = lat1 - lat2;
        double b = (lat1 + lat2) / 2.0;
        // 东西距离
        double lx = FastMath.toRadians(dx) * R * Math.cos(FastMath.toRadians(b));
        // 南北距离
        double ly = R * FastMath.toRadians(dy);
        return Math.sqrt(lx * lx + ly * ly);
    }
}
