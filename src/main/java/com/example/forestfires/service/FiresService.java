package com.example.forestfires.service;

import com.example.forestfires.algorithm.DistanceCal;
import com.example.forestfires.dao.mapper.NearbyTreesMapper;
import com.example.forestfires.dao.mapper.TreesMapper;
import com.example.forestfires.domain.po.TreesDistPO;
import com.example.forestfires.domain.po.TreesPO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;

/**
 * @author zhangduo
 * @date: 2021/7/4 19:58
 */
@Service
public class FiresService {

    @Resource
    private TreesMapper treesMapper;
    @Resource
    private NearbyTreesMapper nearbyTreesMapper;
    @Resource
    public SqlSessionFactory sqlSessionFactory;

    private int BATCH_INSERT_SIZE = 5000;

    // 树的最大直径
    private Double MAX_CROWNDIAMETER;

    public List<TreesPO> listAllFires() {
        return treesMapper.alltrees();
    }

    public void init(Double fireRadiusMultiple) {


        List<TreesPO> treesList = treesMapper.alltrees();

        Map<String, Object> baseLcationMap = treesMapper.peekPoint();
        Double baseLocationX = (Double) baseLcationMap.get("tree_location_x");
        Double baseLocationY = (Double) baseLcationMap.get("tree_location_y");
        MAX_CROWNDIAMETER = (Double) baseLcationMap.get("max_crowndiameter");

        for (TreesPO treesPO : treesList) {
            treesPO.setAuxiliaryDistance(
                DistanceCal.distanceSimplify(
                    treesPO.getTreeLocationX(), treesPO.getTreeLocationY(),
                    baseLocationX, baseLocationY
            ));
        }

        treesList.sort(Comparator.comparing(TreesPO::getAuxiliaryDistance));

        nearbyTreesMapper.cleanNearbyTreesTable();

        List<TreesDistPO> treesDistPOList = new ArrayList<>();
        for (int i = 0; i < treesList.size(); i++) {
            for (int j = 0; j < treesList.size(); j++) {
                // 自己和自己不用比较
                if (i == j) { continue; }

                TreesPO x = treesList.get(i);
                TreesPO y = treesList.get(j);
                // 如果相对距离小于树的最大直径
                if (Math.abs(x.getAuxiliaryDistance() - y.getAuxiliaryDistance()) <= MAX_CROWNDIAMETER) {
                    // 如果海拔差大就不考虑
                    if ((x.getTreeheight() + x.getTreeLocationNz()) < y.getTreeLocationNz()) {
                        continue;
                    }
                    double realDistince = DistanceCal.distanceSimplify(x.getTreeLocationX(), x.getTreeLocationY(), y.getTreeLocationX(), y.getTreeLocationY());
                    // 如果实际距离小于两树的树冠距离
                    if (realDistince < (x.getCrowndiameter()/2 + y.getCrowndiameter()/2 * fireRadiusMultiple)) {
                        treesDistPOList.add(new TreesDistPO(x.getTreeid(), y.getTreeid(), realDistince));
                        if (treesDistPOList.size() == BATCH_INSERT_SIZE) {
                            batchInsert(treesDistPOList);
                        }
                    }
                    // 因为已排序,后面越来越远就不用算了
                } else if (y.getAuxiliaryDistance() - x.getAuxiliaryDistance() > MAX_CROWNDIAMETER){
                    break;
                }
            }
        }
    }


    /**
     * 批量插入数据库
     * @param treesDistPOList
     */
    private void batchInsert(List<TreesDistPO> treesDistPOList) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)){
            NearbyTreesMapper mapper = sqlSession.getMapper(NearbyTreesMapper.class);
            for (TreesDistPO treesDistPO : treesDistPOList) {
                mapper.batchInsertNearbyTrees(treesDistPO);
            }
            sqlSession.commit();
            treesDistPOList.clear();
        }
    }
}
