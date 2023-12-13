package com.lcl.poolchat.common.user.service.impl;

import com.lcl.poolchat.common.common.utils.AssertUtil;
import com.lcl.poolchat.common.user.domain.enums.OssSceneEnum;
import com.lcl.poolchat.common.user.domain.vo.request.oss.UploadUrlReq;
import com.lcl.poolchat.common.user.service.OssService;
import com.lcl.poolchat.oss.MinIOTemplate;
import com.lcl.poolchat.oss.domain.OssReq;
import com.lcl.poolchat.oss.domain.OssResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description:
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-06-20
 */
@Service
public class OssServiceImpl implements OssService {
    @Autowired
    private MinIOTemplate minIOTemplate;

    @Override
    public OssResp getUploadUrl(Long uid, UploadUrlReq req) {
        OssSceneEnum sceneEnum = OssSceneEnum.of(req.getScene());
        AssertUtil.isNotEmpty(sceneEnum, "场景有误");
        OssReq ossReq = OssReq.builder()
                .fileName(req.getFileName())
                .filePath(sceneEnum.getPath())
                .uid(uid)
                .build();
        return minIOTemplate.getPreSignedObjectUrl(ossReq);
    }
}
