package com.lcl.poolchat.common.user.consumer;

import com.lcl.poolchat.common.common.constant.MQConstant;
import com.lcl.poolchat.common.common.domain.dto.PushMessageDTO;
import com.lcl.poolchat.common.user.domain.enums.WSPushTypeEnum;
import com.lcl.poolchat.common.user.service.WebSocketService;
import org.apache.rocketmq.spring.annotation.MessageModel;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description:
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-08-12
 */
@RocketMQMessageListener(topic = MQConstant.PUSH_TOPIC, consumerGroup = MQConstant.PUSH_GROUP, messageModel = MessageModel.BROADCASTING)
@Component
public class PushConsumer implements RocketMQListener<PushMessageDTO> {
    @Autowired
    private WebSocketService webSocketService;

    @Override
    public void onMessage(PushMessageDTO message) {
        WSPushTypeEnum wsPushTypeEnum = WSPushTypeEnum.of(message.getPushType());
        switch (wsPushTypeEnum) {
            case USER:
                message.getUidList().forEach(uid -> {
                    webSocketService.sendToUid(message.getWsBaseMsg(), uid);
                });
                break;
            case ALL:
                webSocketService.sendToAllOnline(message.getWsBaseMsg(), null);
                break;
        }
    }
}
