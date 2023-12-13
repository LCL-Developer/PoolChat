package com.lcl.poolchat.common.user.service;

import com.lcl.poolchat.common.user.domain.vo.request.oss.UploadUrlReq;
import com.lcl.poolchat.oss.domain.OssResp;

/**
 * <p>
 * oss 服务类
 * </p>
 *
 * @author <a href="https://github.com/LCL-Developer">lcl</a>
 * @since 2023-03-19
 */
public interface OssService {

    /**
     * 获取临时的上传链接
     */
    OssResp getUploadUrl(Long uid, UploadUrlReq req);
}
