package com.example.forestfires.service;

import com.example.forestfires.algorithm.DistanceCal;
import com.example.forestfires.dao.mapper.NearbyTreesMapper;
import com.example.forestfires.dao.mapper.TreesMapper;
import com.example.forestfires.domain.TreeStatusEnum;
import com.example.forestfires.domain.po.NearByTreesPO;
import com.example.forestfires.domain.po.TreesPO;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.annotation.Resource;
import org.apache.commons.math3.util.FastMath;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private int BATCH_SIZE = 5000;

    // 树的最大直径
    private Double MAX_CROWNDIAMETER;

    public List<TreesPO> listAllFires() {
        return treesMapper.alltrees();
    }

    @Transactional(rollbackFor = Exception.class)
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
        treesMapper.clearTreeStatus();

        List<NearByTreesPO> nearByTreesPOList = new ArrayList<>();
        for (int i = 0; i < treesList.size(); i++) {
            for (int j = 0; j < treesList.size(); j++) {
                // 自己和自己不用比较
                if (i == j) { continue; }

                TreesPO x = treesList.get(i);
                TreesPO y = treesList.get(j);
                // 如果相对距离小于树的最大直径
                if (Math.abs(x.getAuxiliaryDistance() - y.getAuxiliaryDistance()) <= MAX_CROWNDIAMETER) {
                    // 如果海拔差大就不考虑
                    if ((x.getTreeheight() + x.getTreeLocationNz()) < y.getTreeLocationNz()
                        || y.getTreeLocationNz() + y.getTreeheight() < x.getTreeLocationNz()
                    ) {
                        continue;
                    }
                    double realDistince = DistanceCal.distanceSimplify(x.getTreeLocationX(), x.getTreeLocationY(), y.getTreeLocationX(), y.getTreeLocationY());
                    // 如果实际距离小于两树的树冠距离
                    if (realDistince < (x.getCrowndiameter()/2 + y.getCrowndiameter()/2 * fireRadiusMultiple)) {
                        nearByTreesPOList.add(
                            NearByTreesPO.of(
                                x.getTreeid(),
                                y.getTreeid(),
                                realDistince,
                                FastMath.toDegrees(FastMath.atan2(x.getTreeLocationNz() - y.getTreeLocationNz(), realDistince)),
                                DistanceCal.calAngle(x.getTreeLocationX(), x.getTreeLocationY(), y.getTreeLocationX(), y.getTreeLocationY()),
                                TreeStatusEnum.NOT_FIRE.getStatus())
                        );
                        if (nearByTreesPOList.size() == BATCH_SIZE) {
                            batchinsertNearbyTrees(nearByTreesPOList);
                        }
                    }
                    // 因为已排序,后面越来越远就不用算了
                } else if (y.getAuxiliaryDistance() - x.getAuxiliaryDistance() > MAX_CROWNDIAMETER){
                    break;
                }
            }
        }
        if (!nearByTreesPOList.isEmpty()) {
            batchinsertNearbyTrees(nearByTreesPOList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void startFire(int startFireID) {
        treesMapper.updateTreeStatus(TreeStatusEnum.FIRE.getStatus(), startFireID);
        nearbyTreesMapper.updateNearbyTreeStatus(TreeStatusEnum.FIRE.getStatus(),  startFireID);
    }

    public List<TreesPO> nextFire(String simulatedtime) {
        List<TreesPO> treesPOList = new ArrayList<>();
        List<NearByTreesPO> possibleFireTreeList = nearbyTreesMapper.possibleFireTrees();

        Map<Integer, List<NearByTreesPO>> treeListMap = new HashMap<>();
        for (NearByTreesPO nearByTreesPO : possibleFireTreeList) {
            Integer treeid = nearByTreesPO.getTreeid();
            treeListMap.computeIfAbsent(treeid, key -> new ArrayList<>());
            treeListMap.get(treeid).add(nearByTreesPO);
        }

        List<Integer> fireTreeidList = new ArrayList<>();
        for (Integer treeid : treeListMap.keySet()) {
            if (calPossibleFireTree(treeid, treeListMap.get(treeid))) {
                fireTreeidList.add(treeid);
            }
        }
        if (!fireTreeidList.isEmpty()) {
            batchUpdateTreeStatus(fireTreeidList);
            treesPOList = treesMapper.treesInfo(fireTreeidList);
        }

        // 灭火的树列表
        List<TreesPO> unfiredTrees = treesMapper.toBeUnfiredTrees(simulatedtime);
        if (!unfiredTrees.isEmpty()) {
            treesPOList.addAll(unfiredTrees);
            treesMapper.updateUnfiredTreesStatus();
        }

        return treesPOList;
    }

    private void batchUpdateTreeStatus(List<Integer> fireTreeidList) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)) {
            NearbyTreesMapper nearbyTreesMapper = sqlSession.getMapper(NearbyTreesMapper.class);
            TreesMapper treesMapper = sqlSession.getMapper(TreesMapper.class);

            for (Integer treeid : fireTreeidList) {
                nearbyTreesMapper.updateNearbyTreeStatus(TreeStatusEnum.FIRE.getStatus(), treeid);
                treesMapper.updateTreeStatus(TreeStatusEnum.FIRE.getStatus(), treeid);
            }
            sqlSession.commit();
        }
    }

    private boolean calPossibleFireTree(int treeid, List<NearByTreesPO> nearbyTreeList) {
        Random random = new Random();
        return random.nextBoolean();
    }

    /**
     * 批量插入数据库
     * @param nearByTreesPOList
     */
    private void batchinsertNearbyTrees(List<NearByTreesPO> nearByTreesPOList) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH, false)){
            NearbyTreesMapper mapper = sqlSession.getMapper(NearbyTreesMapper.class);
            for (NearByTreesPO nearByTreesPO : nearByTreesPOList) {
                mapper.batchInsertNearbyTrees(nearByTreesPO);
            }
            sqlSession.commit();
            nearByTreesPOList.clear();
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void resetFireStatus() {
        treesMapper.clearTreeStatus();
        nearbyTreesMapper.resetNearbyTreesStatus();
    }
}
