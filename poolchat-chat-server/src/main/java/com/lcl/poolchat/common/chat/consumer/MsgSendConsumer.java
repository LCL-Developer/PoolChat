package com.lcl.poolchat.common.chat.consumer;

import com.lcl.poolchat.common.chat.dao.ContactDao;
import com.lcl.poolchat.common.chat.dao.MessageDao;
import com.lcl.poolchat.common.chat.dao.RoomDao;
import com.lcl.poolchat.common.chat.dao.RoomFriendDao;
import com.lcl.poolchat.common.chat.domain.entity.Message;
import com.lcl.poolchat.common.chat.domain.entity.Room;
import com.lcl.poolchat.common.chat.domain.entity.RoomFriend;
import com.lcl.poolchat.common.chat.domain.enums.RoomTypeEnum;
import com.lcl.poolchat.common.chat.domain.vo.response.ChatMessageResp;
import com.lcl.poolchat.common.chat.service.ChatService;
import com.lcl.poolchat.common.chat.service.WeChatMsgOperationService;
import com.lcl.poolchat.common.chat.service.cache.GroupMemberCache;
import com.lcl.poolchat.common.chat.service.cache.HotRoomCache;
import com.lcl.poolchat.common.chat.service.cache.RoomCache;
import com.lcl.poolchat.common.chatai.service.IChatAIService;
import com.lcl.poolchat.common.common.constant.MQConstant;
import com.lcl.poolchat.common.common.domain.dto.MsgSendMessageDTO;
import com.lcl.poolchat.common.user.service.WebSocketService;
import com.lcl.poolchat.common.user.service.adapter.WSAdapter;
import com.lcl.poolchat.common.user.service.cache.UserCache;
import com.lcl.poolchat.common.user.service.impl.PushService;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Description: 发送消息更新房间收信箱，并同步给房间成员信箱
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-08-12
 */
@RocketMQMessageListener(consumerGroup = MQConstant.SEND_MSG_GROUP, topic = MQConstant.SEND_MSG_TOPIC)
@Component
public class MsgSendConsumer implements RocketMQListener<MsgSendMessageDTO> {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageDao messageDao;
    @Autowired
    private IChatAIService openAIService;
    @Autowired
    WeChatMsgOperationService weChatMsgOperationService;
    @Autowired
    private RoomCache roomCache;
    @Autowired
    private RoomDao roomDao;
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private UserCache userCache;
    @Autowired
    private RoomFriendDao roomFriendDao;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private ContactDao contactDao;
    @Autowired
    private HotRoomCache hotRoomCache;
    @Autowired
    private PushService pushService;

    @Override
    public void onMessage(MsgSendMessageDTO dto) {
        Message message = messageDao.getById(dto.getMsgId());
        Room room = roomCache.get(message.getRoomId());
        ChatMessageResp msgResp = chatService.getMsgResp(message, null);
        //所有房间更新房间最新消息
        roomDao.refreshActiveTime(room.getId(), message.getId(), message.getCreateTime());
        roomCache.delete(room.getId());
        if (room.isHotRoom()) {//热门群聊推送所有在线的人
            //更新热门群聊时间-redis
            hotRoomCache.refreshActiveTime(room.getId(), message.getCreateTime());
            //推送所有人
            pushService.sendPushMsg(WSAdapter.buildMsgSend(msgResp));
        } else {
            List<Long> memberUidList = new ArrayList<>();
            if (Objects.equals(room.getType(), RoomTypeEnum.GROUP.getType())) {//普通群聊推送所有群成员
                memberUidList = groupMemberCache.getMemberUidList(room.getId());
            } else if (Objects.equals(room.getType(), RoomTypeEnum.FRIEND.getType())) {//单聊对象
                //对单人推送
                RoomFriend roomFriend = roomFriendDao.getByRoomId(room.getId());
                memberUidList = Arrays.asList(roomFriend.getUid1(), roomFriend.getUid2());
            }
            //更新所有群成员的会话时间,没有就去创建窗口
            contactDao.refreshOrCreateActiveTime(room.getId(), memberUidList, message.getId(), message.getCreateTime());
            //推送房间成员
            pushService.sendPushMsg(WSAdapter.buildMsgSend(msgResp), memberUidList);
        }
    }


}
