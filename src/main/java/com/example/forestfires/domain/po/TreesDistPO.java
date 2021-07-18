package com.example.forestfires.domain.po;

import lombok.Data;
import lombok.NonNull;

/**
 * @author zhangduo
 * @date: 2021/7/12 22:07
 */
@Data
public class TreesDistPO {

    @NonNull
    private Integer treeID;
    @NonNull
    private Integer nearbyTreeID;
    @NonNull
    private Double distince;

}
