package com.lcl.poolchat.common.user.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Description: 物品枚举
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-03-19
 */
@AllArgsConstructor
@Getter
public enum BlackTypeEnum {
    IP(1),
    UID(2),
    ;

    private final Integer type;

}
