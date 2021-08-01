package com.example.forestfires.algorithm;

import com.example.forestfires.domain.FireCondition;
import java.util.Optional;
import org.apache.commons.math3.util.FastMath;

/**
 * @author zhangduo
 * @date: 2021/7/28 22:33
 */
public class FireSpeed {

    /**
     * 速度计算 R0 = aT + bW － c
     * @param t
     * @param w
     * @param a
     * @param b
     * @param c
     * @param angle 坐标角度
     * @param slope 坡度
     * @return
     */
    public static double calFireSpeed(FireCondition fireCondition, double angle, double slope) {

        double a = Optional.ofNullable(fireCondition.getA()).orElse(0.053);
        double b = Optional.ofNullable(fireCondition.getB()).orElse(0.048);
        double c = Optional.ofNullable(fireCondition.getC()).orElse(0.275);
        double speed = a * fireCondition.getT() + b * fireCondition.getW() -  c;
        double angleFactor = 1;
        double slopeFactor = 1;

        double windAngle =  Optional.ofNullable(fireCondition.getWindAngle()).orElse(0d);
        double absAngle = FastMath.abs(windAngle - angle);
        // 风方向
        if (absAngle == 0 || absAngle == 360) {
            angleFactor = 1;
        } else if (absAngle <= 45) {
            angleFactor = 0.8;
        } else if (absAngle <= 90) {
            angleFactor = 0.6;
        } else if (absAngle <= 135) {
            angleFactor =  0.4;
        } else if (absAngle < 180) {
            angleFactor = 0.2;
        } else if (absAngle == 180) {
            angleFactor = 0.1;
        } else if (absAngle <= 225) {
            angleFactor = 0.2;
        } else if (absAngle <= 270) {
            angleFactor = 0.4;
        } else if (absAngle < 360) {
            angleFactor = 0.8;
        }

        // 坡度
        if (slope == 0) {
            slopeFactor = 1;
        } else if (slope > 0) {
            if (slope <= 30) {
                slopeFactor = 1.5;
            } else if (slope <= 60) {
                slopeFactor = 3;
            } else if (slope <= 90) {
                slopeFactor = 4.5;
            }
        } else {
            slope = - slope;
            if (slope <= 30) {
                slopeFactor = 0.7;
            } else if (slope <= 60) {
                slopeFactor = 0.4;
            } else if (slope <= 90) {
                slopeFactor = 0.2;
            }
        }


        return speed * angleFactor * slopeFactor;
    }

}
