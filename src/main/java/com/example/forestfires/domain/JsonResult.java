package com.example.forestfires.domain;

import lombok.Data;

/**
 * @author zhangduo
 * @date: 2021/7/11 16:53
 */
@Data
public class JsonResult<T> {

    private T data;
    private Integer code;
    private String msg;

    public JsonResult(T data) {
        this.data = data;
        this.code = 200;
        this.msg = "success";
    }

}
