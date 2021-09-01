package com.example.forestfires.controller;

import com.example.forestfires.domain.FireCondition;
import com.example.forestfires.domain.JsonResult;
import com.example.forestfires.domain.po.TreesPO;
import com.example.forestfires.service.FiresService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangduo
 * @date: 2021/7/4 19:21
 */
@RestController
@RequestMapping("/fires")
public class FiresRestful {

    @Resource
    private FiresService firesService;

    @GetMapping("allfires")
    @ApiOperation("所有的树节点")
    public ResponseEntity<JsonResult<?>> queryAllfires(){
        List<TreesPO> treeList = firesService.listAllFires();
        JsonResult<List<TreesPO>> jsonResult = new JsonResult<>(treeList);
        return new ResponseEntity<>(jsonResult, HttpStatus.OK);
    }

    @PostMapping("init")
    @ApiOperation("初始化")
    public void init(@RequestBody Map<String,String> params) {
        double fireRadiusMultiple = Double.parseDouble(params.getOrDefault("fireRadiusMultiple", "1.5"));
        firesService.init(fireRadiusMultiple);
    }

    @PostMapping("startfire")
    @ApiOperation("开始起火")
    public void startfire(@RequestBody Map<String,String> params) {
        firesService.startFire(Integer.parseInt(params.get("treeid")));
    }

    @PostMapping("nextFire")
    @ApiOperation("nextFire")
    public ResponseEntity<JsonResult<?>> nextFire(@RequestBody FireCondition fireCondition) {
        System.out.println(fireCondition);
        JsonResult<List<TreesPO>> jsonResult = new JsonResult<>(firesService.nextFire(fireCondition));
        return new ResponseEntity<>(jsonResult, HttpStatus.OK);
    }

    @PostMapping("resetFire")
    @ApiOperation("重置状态")
    public void resetfire() {
        firesService.resetFireStatus();
    }

    @GetMapping("fireline")
    @ApiOperation("火线")
    public ResponseEntity<JsonResult<?>> fineLine() {
        JsonResult<List<TreesPO>> jsonResult = new JsonResult<>(firesService.getFireLine());
        return new ResponseEntity<>(jsonResult, HttpStatus.OK);
    }
}
