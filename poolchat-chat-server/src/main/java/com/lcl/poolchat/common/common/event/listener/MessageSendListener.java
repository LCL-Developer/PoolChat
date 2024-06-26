package com.lcl.poolchat.common.common.event.listener;

import com.lcl.poolchat.common.chat.dao.ContactDao;
import com.lcl.poolchat.common.chat.dao.MessageDao;
import com.lcl.poolchat.common.chat.dao.RoomDao;
import com.lcl.poolchat.common.chat.dao.RoomFriendDao;
import com.lcl.poolchat.common.chat.domain.entity.Message;
import com.lcl.poolchat.common.chat.domain.entity.Room;
import com.lcl.poolchat.common.chat.domain.enums.HotFlagEnum;
import com.lcl.poolchat.common.chat.service.ChatService;
import com.lcl.poolchat.common.chat.service.WeChatMsgOperationService;
import com.lcl.poolchat.common.chat.service.cache.GroupMemberCache;
import com.lcl.poolchat.common.chat.service.cache.HotRoomCache;
import com.lcl.poolchat.common.chat.service.cache.RoomCache;
import com.lcl.poolchat.common.chatai.service.IChatAIService;
import com.lcl.poolchat.common.common.constant.MQConstant;
import com.lcl.poolchat.common.common.domain.dto.MsgSendMessageDTO;
import com.lcl.poolchat.common.common.event.MessageSendEvent;
import com.lcl.poolchat.common.user.service.WebSocketService;
import com.lcl.poolchat.common.user.service.cache.UserCache;
import com.lcl.poolchat.transaction.service.MQProducer;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.Objects;

/**
 * 消息发送监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
public class MessageSendListener {
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
    private MQProducer mqProducer;

    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT, classes = MessageSendEvent.class, fallbackExecution = true)
    public void messageRoute(MessageSendEvent event) {
        Long msgId = event.getMsgId();
        mqProducer.sendSecureMsg(MQConstant.SEND_MSG_TOPIC, new MsgSendMessageDTO(msgId), msgId);
    }

    /**
     * openAI 机器人被@后，进行消息回复
     * @param event
     */
    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
    public void handlerMsg(@NotNull MessageSendEvent event) {
        Message message = messageDao.getById(event.getMsgId());
        Room room = roomCache.get(message.getRoomId());
        if (isHotRoom(room)) {
            openAIService.chat(message);
        }
    }

    public boolean isHotRoom(Room room) {
        return Objects.equals(HotFlagEnum.YES.getType(), room.getHotFlag());
    }

    /**
     * 给用户微信推送艾特好友的消息通知
     * （这个没开启，微信不让推）
     */
    @TransactionalEventListener(classes = MessageSendEvent.class, fallbackExecution = true)
    public void publishChatToWechat(@NotNull MessageSendEvent event) {
        Message message = messageDao.getById(event.getMsgId());
        if (Objects.nonNull(message.getExtra().getAtUidList())) {
            weChatMsgOperationService.publishChatMsgToWeChatUser(message.getFromUid(), message.getExtra().getAtUidList(),
                    message.getContent());
        }
    }
}
