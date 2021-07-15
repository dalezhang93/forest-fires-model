package com.example.forestfires.service;

import com.example.forestfires.algorithm.DistanceCal;
import com.example.forestfires.dao.mapper.TreesMapper;
import com.example.forestfires.domain.po.TreesDistPO;
import com.example.forestfires.domain.po.TreesPO;
import java.util.ArrayList;
import java.util.Comparator;
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

    public void init() {


        List<TreesPO> treesList = treesMapper.alltrees();

        Map<String, Object> baseLcationMap = treesMapper.peekPoint();
        Double baseLocationX = (Double) baseLcationMap.get("tree_location_x");
        Double baseLocationY = (Double) baseLcationMap.get("tree_location_y");

        for (TreesPO treesPO : treesList) {
            treesPO.setAuxiliaryDistance(
                DistanceCal.distanceSimplify(
                    treesPO.getTreeLocationX(), treesPO.getTreeLocationY(),
                    baseLocationX, baseLocationY
            ));
        }

        treesList.sort(Comparator.comparing(TreesPO::getAuxiliaryDistance));

        List<TreesDistPO> treesDistPOList = new ArrayList<>();
        for (int i = 0; i < treesList.size(); i++) {
            for (int j = 0; j < treesList.size(); j++) {
                // 自己和自己不用比较
                if (i == j) { continue; }

                TreesPO x = treesList.get(i);
                TreesPO y = treesList.get(j);
                // 如果相对距离小于 20(临时定为 20)
                if (Math.abs(x.getAuxiliaryDistance() - y.getAuxiliaryDistance()) < 20) {
                    double realDistince = DistanceCal.distanceSimplify(x.getTreeLocationX(), x.getTreeLocationY(), y.getTreeLocationX(), y.getTreeLocationY());
                    if (realDistince < 20) {
                        if ((x.getTreeheight() + x.getTreeLocationNz()) < y.getTreeLocationNz()) {
                            continue;
                        }
                        treesDistPOList.add(new TreesDistPO(x.getTreeid(), y.getTreeid(), realDistince));
                    }
                    // 因为已排序,后面越来越远就不用算了
                } else if (y.getAuxiliaryDistance() - x.getAuxiliaryDistance() > 20){
                    break;
                }
            }
        }
    }
}
