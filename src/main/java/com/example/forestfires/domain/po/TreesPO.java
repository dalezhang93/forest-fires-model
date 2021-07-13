package com.example.forestfires.domain.po;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

/**
 * @author zhangduo
 * @date: 2021/7/4 19:38
 */
@Data
public class TreesPO {
    private Integer treeid ;
    private Double treeLocationX ;
    private Double treeLocationY ;
    private Integer treeheight ;
    private Double crowndiameter ;
    private Double crownarea ;
    private Integer crownvolume ;
    private Integer previousid ;
    private Integer treeLocationNz;
    @JsonIgnore
    private Double auxiliaryDistance ;
    private Integer status;
}
