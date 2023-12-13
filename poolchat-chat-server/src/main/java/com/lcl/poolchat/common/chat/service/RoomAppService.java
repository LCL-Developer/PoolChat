package com.lcl.poolchat.common.chat.service;

import com.lcl.poolchat.common.chat.domain.vo.request.*;
import com.lcl.poolchat.common.chat.domain.vo.request.*;
import com.lcl.poolchat.common.chat.domain.vo.response.ChatMemberListResp;
import com.lcl.poolchat.common.chat.domain.vo.response.ChatRoomResp;
import com.lcl.poolchat.common.chat.domain.vo.response.MemberResp;
import com.lcl.poolchat.common.common.domain.vo.request.CursorPageBaseReq;
import com.lcl.poolchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.lcl.poolchat.common.user.domain.vo.response.ws.ChatMemberResp;

import java.util.List;

/**
 * Description:
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-07-22
 */
public interface RoomAppService {
    /**
     * 获取会话列表--支持未登录态
     */
    CursorPageBaseResp<ChatRoomResp> getContactPage(CursorPageBaseReq request, Long uid);

    /**
     * 获取群组信息
     */
    MemberResp getGroupDetail(Long uid, long roomId);

    CursorPageBaseResp<ChatMemberResp> getMemberPage(MemberReq request);

    List<ChatMemberListResp> getMemberList(ChatMessageMemberReq request);

    void delMember(Long uid, MemberDelReq request);

    void addMember(Long uid, MemberAddReq request);

    Long addGroup(Long uid, GroupAddReq request);

    ChatRoomResp getContactDetail(Long uid, Long roomId);

    ChatRoomResp getContactDetailByFriend(Long uid, Long friendUid);
}
