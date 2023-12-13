package com.lcl.poolchat.common.common.event.listener;

import com.lcl.poolchat.common.chat.dao.GroupMemberDao;
import com.lcl.poolchat.common.chat.domain.entity.GroupMember;
import com.lcl.poolchat.common.chat.domain.entity.RoomGroup;
import com.lcl.poolchat.common.chat.domain.vo.request.ChatMessageReq;
import com.lcl.poolchat.common.chat.service.ChatService;
import com.lcl.poolchat.common.chat.service.adapter.MemberAdapter;
import com.lcl.poolchat.common.chat.service.adapter.RoomAdapter;
import com.lcl.poolchat.common.chat.service.cache.GroupMemberCache;
import com.lcl.poolchat.common.chat.service.cache.MsgCache;
import com.lcl.poolchat.common.common.event.GroupMemberAddEvent;
import com.lcl.poolchat.common.user.dao.UserDao;
import com.lcl.poolchat.common.user.domain.entity.User;
import com.lcl.poolchat.common.user.domain.enums.WSBaseResp;
import com.lcl.poolchat.common.user.domain.vo.response.ws.WSMemberChange;
import com.lcl.poolchat.common.user.service.WebSocketService;
import com.lcl.poolchat.common.user.service.cache.UserInfoCache;
import com.lcl.poolchat.common.user.service.impl.PushService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 添加群成员监听器
 *
 * @author zhongzb create on 2022/08/26
 */
@Slf4j
@Component
public class GroupMemberAddListener {
    @Autowired
    private WebSocketService webSocketService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MsgCache msgCache;
    @Autowired
    private UserInfoCache userInfoCache;
    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;
    @Autowired
    private GroupMemberDao groupMemberDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private GroupMemberCache groupMemberCache;
    @Autowired
    private PushService pushService;

    /**
     * 发送给群内人员 邀请信息
     */
    @Async
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendAddMsg(GroupMemberAddEvent event) {
        List<GroupMember> memberList = event.getMemberList();
        RoomGroup roomGroup = event.getRoomGroup();
        Long inviteUid = event.getInviteUid();
        User user = userInfoCache.get(inviteUid);
        List<Long> uidList = memberList.stream().map(GroupMember::getUid).collect(Collectors.toList());
        ChatMessageReq chatMessageReq = RoomAdapter.buildGroupAddMessage(roomGroup, user, userInfoCache.getBatch(uidList));
        chatService.sendMsg(chatMessageReq, User.UID_SYSTEM);
    }

    /**
     * 发送给被邀请用户 加入消息
     */
    @Async
    @TransactionalEventListener(classes = GroupMemberAddEvent.class, fallbackExecution = true)
    public void sendChangePush(GroupMemberAddEvent event) {
        List<GroupMember> memberList = event.getMemberList();
        RoomGroup roomGroup = event.getRoomGroup();
        List<Long> memberUidList = groupMemberCache.getMemberUidList(roomGroup.getRoomId());
        List<Long> uidList = memberList.stream().map(GroupMember::getUid).collect(Collectors.toList());
        List<User> users = userDao.listByIds(uidList);
        users.forEach(user -> {
            WSBaseResp<WSMemberChange> ws = MemberAdapter.buildMemberAddWS(roomGroup.getRoomId(), user);
            pushService.sendPushMsg(ws, memberUidList);
        });
        //移除缓存
        groupMemberCache.evictMemberUidList(roomGroup.getRoomId());
    }

}
