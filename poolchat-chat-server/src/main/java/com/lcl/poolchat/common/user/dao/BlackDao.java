package com.lcl.poolchat.common.user.dao;

import com.lcl.poolchat.common.user.domain.entity.Black;
import com.lcl.poolchat.common.user.mapper.BlackMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 黑名单 服务实现类
 * </p>
 *
 * @author <a href="https://github.com/LCL-Developer">lcl</a>
 * @since 2023-05-21
 */
@Service
public class BlackDao extends ServiceImpl<BlackMapper, Black> {

}
