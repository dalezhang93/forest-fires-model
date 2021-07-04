package com.example.forestfires.controller;

import com.example.forestfires.domain.po.TreesPO;
import com.example.forestfires.service.FiresService;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
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
    public List<TreesPO> queryAllfires(){
        return firesService.listAllFires();
    }

    @GetMapping("init")
    public List<TreesPO> init() {
        return firesService.init();
    }
}
