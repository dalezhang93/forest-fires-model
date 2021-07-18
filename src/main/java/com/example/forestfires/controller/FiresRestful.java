package com.example.forestfires.controller;

import com.example.forestfires.domain.JsonResult;
import com.example.forestfires.domain.po.TreesPO;
import com.example.forestfires.service.FiresService;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public ResponseEntity<JsonResult> queryAllfires(){
        List<TreesPO> treesPOList = firesService.listAllFires();
        JsonResult<List<TreesPO>> jsonResult = new JsonResult<>(treesPOList);
        return new ResponseEntity<>(jsonResult, HttpStatus.OK);
    }

    @PostMapping("init")
    @ApiOperation("初始化")
    public void init(@RequestParam(value = "fireRadiusMultiple", defaultValue = "1.5") Double fireRadiusMultiple) {
        firesService.init(fireRadiusMultiple);
    }
}
