package com.example.forestfires.domain.po;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zhangduo
 * @date: 2021/7/12 22:07
 */
@Data
@AllArgsConstructor(staticName = "of")
public class NearByTreesPO {

    private Integer treeid;
    private Integer nearbytreeid;
    private Double distince;
    // 坡度
    private Double slope;
    // 角度
    private Double angle;
    private Integer nearbytreeStatus;
    private String nearybyStartFireTime;

}
