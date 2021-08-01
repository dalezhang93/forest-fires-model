package com.example.forestfires.domain;

import lombok.Data;

/**
 * @author zhangduo
 * @date: 2021/8/1 22:08
 */
@Data
public class FireCondition {
    private String simulatedtime;
    // 速度计算 R0 = aT + bW － c, T 为温度( ℃ ) ; W 为风力( 级)
    private Double T;
    private Double a;
    private Double b;
    private Integer W;
    private Double c;
    // 风向角度
    private Double windAngle;
}
