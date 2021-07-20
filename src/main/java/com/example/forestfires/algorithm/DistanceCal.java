package com.example.forestfires.algorithm;

import java.util.EnumMap;
import java.util.Map;
import org.apache.commons.math3.util.FastMath;

/**
 * @author zhangduo
 * @date: 2021/7/4 22:16
 */
public class DistanceCal {
    private static final double R = 6367000.0;

    public enum DistinceEnum {
        // 直线距离
        STRAIGHT_DISTINCE,
        // 角度
        ANGLE
    }

    public static double distanceSimplify(double lat1, double lng1, double lat2, double lng2) {
        // 经度差值
        double dx = lng1 - lng2;
        // 纬度差值
        double dy = lat1 - lat2;
        double b = (lat1 + lat2) / 2.0;
        // 东西距离
        double lx = FastMath.toRadians(dx) * R * FastMath.cos(FastMath.toRadians(b));
        // 南北距离
        double ly = R * FastMath.toRadians(dy);
        return FastMath.sqrt(lx * lx + ly * ly);
    }

    public static double calAngle(double lat1, double lng1, double lat2, double lng2) {
        // 经度差值
        double dx = lng1 - lng2;
        // 纬度差值
        double dy = lat1 - lat2;
        // 角度计算
        double angle;
        if (dx == 0 && dy > 0) {
            angle = 0d;
        } else if (dx == 0 && dy < 0) {
            angle = 180d;
        } else if (dy == 0 && dx > 0) {
            angle = 90d;
        } else if (dy == 0 && dx < 0) {
            angle = 270d;
        } else {
            double b = (lat1 + lat2) / 2.0;
            // 东西距离
            double lx = FastMath.toRadians(dx) * R * FastMath.cos(FastMath.toRadians(b));
            // 南北距离
            double ly = R * FastMath.toRadians(dy);
            angle = FastMath.toDegrees(FastMath.atan2(ly, lx));
        }
        return angle;
    }

    public static Map<DistinceEnum, Double> distanceFull(double lat1, double lng1, double lat2, double lng2) {

        Map<DistinceEnum, Double> distinceEnumMap = new EnumMap<>(DistinceEnum.class);
        // 经度差值
        double dx = lng1 - lng2;
        // 纬度差值
        double dy = lat1 - lat2;
        double b = (lat1 + lat2) / 2.0;
        // 东西距离
        double lx = FastMath.toRadians(dx) * R * FastMath.cos(FastMath.toRadians(b));
        // 南北距离
        double ly = R * FastMath.toRadians(dy);
        distinceEnumMap.put(DistinceEnum.STRAIGHT_DISTINCE, FastMath.sqrt(lx * lx + ly * ly));

        // 角度计算
        double angle;
        if (dx == 0 && dy > 0) {
            angle = 0d;
        } else if (dx == 0 && dy < 0) {
            angle = 180d;
        } else if (dy == 0 && dx > 0) {
            angle = 90d;
        } else if (dy == 0 && dx < 0) {
            angle = 270d;
        } else {
            angle = FastMath.toDegrees(FastMath.atan2(ly, lx));
        }
        distinceEnumMap.put(DistinceEnum.ANGLE, angle);
        return distinceEnumMap;
    }
}
