package com.example.forestfires.dao.mapper;

import com.example.forestfires.domain.po.TreesDistPO;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhangduo
 * @date: 2021/7/16 15:09
 */
public interface NearbyTreesMapper {

    /**
     * 批量插入数据库
     * @param TreesDistPO
     */
    void batchInsertNearbyTrees(@Param("tree") TreesDistPO trees);

    /**
     * 清空表
     */
    void cleanNearbyTreesTable();

}