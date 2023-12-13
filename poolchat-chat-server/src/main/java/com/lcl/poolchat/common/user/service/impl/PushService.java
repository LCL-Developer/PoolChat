package com.lcl.poolchat.common.user.service.impl;

import com.lcl.poolchat.common.common.constant.MQConstant;
import com.lcl.poolchat.common.common.domain.dto.PushMessageDTO;
import com.lcl.poolchat.common.user.domain.enums.WSBaseResp;
import com.lcl.poolchat.transaction.service.MQProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Description:
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-08-12
 */
@Service
public class PushService {
    @Autowired
    private MQProducer mqProducer;

    public void sendPushMsg(WSBaseResp<?> msg, List<Long> uidList) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(uidList, msg));
    }

    public void sendPushMsg(WSBaseResp<?> msg, Long uid) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(uid, msg));
    }

    public void sendPushMsg(WSBaseResp<?> msg) {
        mqProducer.sendMsg(MQConstant.PUSH_TOPIC, new PushMessageDTO(msg));
    }
}
