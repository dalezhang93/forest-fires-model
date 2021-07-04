package com.example.forestfires.service;

import com.example.forestfires.algorithm.DistanceCal;
import com.example.forestfires.dao.mapper.TreesMapper;
import com.example.forestfires.domain.po.TreesPO;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * @author zhangduo
 * @date: 2021/7/4 19:58
 */
@Service
public class FiresService {

    @Resource
    private TreesMapper treesMapper;

    public List<TreesPO> listAllFires() {
        return treesMapper.alltrees();
    }

    public List<TreesPO> init() {
        List<TreesPO> treesList = treesMapper.alltrees();
        Map<String, Object> baseLcationMap = treesMapper.peekPoint();
        Double baseLocationX = (Double) baseLcationMap.get("tree_location_x");
        Double baseLocationY = (Double) baseLcationMap.get("tree_location_y");

        for (TreesPO treesPO : treesList) {
            treesPO.setAuxiliaryDistance(DistanceCal.distanceSimplify(
                treesPO.getTreeLocationX(), treesPO.getTreeLocationY(),
                baseLocationX, baseLocationY
            ));
        }
        return treesList;
    }

}
