package com.example.forestfires.dao.mapper;

import com.example.forestfires.domain.po.TreesPO;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;

/**
 * @author zhangduo
 * @date: 2021/7/4 19:44
 */
public interface TreesMapper {

    /**
     * 列举所有Trees
     * @return
     */
    List<TreesPO> alltrees();

    /**
     * 选取其中一个点
     * @return
     */
    Map<String, Object> peekPoint();

    /**
     * 更新 trees 表的状态
     * @param status
     * @param treeid
     */
    void updateTreeStatus(@Param("status") int status, @Param("treeid") int treeid);

    void clearTreeStatus();

}
