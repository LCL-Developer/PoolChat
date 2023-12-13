package com.lcl.poolchat.common.user.dao;

import com.lcl.poolchat.common.user.domain.entity.Role;
import com.lcl.poolchat.common.user.mapper.RoleMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 角色表 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/LCL-Developer">lcl</a>
 * @since 2023-06-04
 */
@Service
public class RoleDao extends ServiceImpl<RoleMapper, Role> {

}
