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
     *
     * @return
     */
    List<TreesPO> treesInfo(@Param("list") List<Integer> list);

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

    /**
     * 将要被 reset 的树
     * @return
     */
    List<TreesPO> toResetTrees();


    /**
     * 将要灭火的树
     * @param simulatedtime
     * @return
     */
    List<TreesPO> toBeUnfiredTrees(@Param("simulatedtime") String simulatedtime);

    /**
     * 更新要灭火的树的状态
     */
    void updateUnfiredTreesStatus();

    /**
     * 火线列表
     * @return
     */
    List<TreesPO> firelineList();

}
