package com.lcl.poolchat.common.chat.service.helper;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import com.lcl.poolchat.common.user.domain.enums.ChatActiveStatusEnum;

/**
 * Description: 成员列表工具类
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-03-28
 */
public class ChatMemberHelper {
    private static final String SEPARATOR = "_";

    /**
     * 解析成员列表的游标
     */
    public static Pair<ChatActiveStatusEnum, String> getCursorPair(String cursor) {
        ChatActiveStatusEnum activeStatusEnum = ChatActiveStatusEnum.ONLINE;
        String timeCursor = null;
        if (StrUtil.isNotBlank(cursor)) {
            String activeStr = cursor.split(SEPARATOR)[0];
            String timeStr = cursor.split(SEPARATOR)[1];
            activeStatusEnum = ChatActiveStatusEnum.of(Integer.parseInt(activeStr));
            timeCursor = timeStr;
        }
        return Pair.of(activeStatusEnum, timeCursor);
    }

    public static String generateCursor(ChatActiveStatusEnum activeStatusEnum, String timeCursor) {
        return activeStatusEnum.getStatus() + SEPARATOR + timeCursor;
    }
}