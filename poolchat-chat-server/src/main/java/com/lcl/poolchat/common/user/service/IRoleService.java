package com.lcl.poolchat.common.user.service;

import com.lcl.poolchat.common.user.domain.enums.RoleEnum;

/**
 * <p>
 * 角色表 服务类
 * </p>
 *
 * @author <a href="https://github.com/LCL-Developer">lcl</a>
 * @since 2023-06-04
 */
public interface IRoleService {

    /**
     * 是否有某个权限，临时做法
     *
     * @return
     */
    boolean hasPower(Long uid, RoleEnum roleEnum);

}
