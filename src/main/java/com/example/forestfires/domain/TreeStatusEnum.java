package com.example.forestfires.domain;

import lombok.Getter;

/**
 * @author zhangduo
 * @date: 2021/7/22 14:44
 */
public enum TreeStatusEnum {
    // 未起火
    NOT_FIRE(0),
    // 起火
    FIRE(1),
    // 灭火
    FIRED(2);

    @Getter
    private final int status;
    TreeStatusEnum(int status) {
        this.status = status;
    }
}
