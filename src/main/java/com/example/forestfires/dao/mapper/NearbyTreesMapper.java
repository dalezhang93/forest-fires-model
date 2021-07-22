package com.example.forestfires.dao.mapper;

import com.example.forestfires.domain.po.NearByTreesPO;
import java.util.List;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhangduo
 * @date: 2021/7/16 15:09
 */
public interface NearbyTreesMapper {

    /**
     * 批量插入数据库
     * @param trees
     */
    void batchInsertNearbyTrees(@Param("tree") NearByTreesPO trees);

    /**
     * 清空表
     */
    void cleanNearbyTreesTable();

    /**
     * 更新 nearbytrrs 表的树状态
     */
    void updateNearbyTreeStatus(@Param("status") int status, @Param("nearbytreeid") int nearbytreeid);

    /**
     * 可能着火的点
     * @return
     */
    List<NearByTreesPO> possibleFireTrees();

}
