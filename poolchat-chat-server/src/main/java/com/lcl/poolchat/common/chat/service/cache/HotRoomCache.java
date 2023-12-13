package com.lcl.poolchat.common.chat.service.cache;

import cn.hutool.core.lang.Pair;
import com.lcl.poolchat.common.common.constant.RedisKey;
import com.lcl.poolchat.common.common.domain.vo.request.CursorPageBaseReq;
import com.lcl.poolchat.common.common.domain.vo.response.CursorPageBaseResp;
import com.lcl.poolchat.common.common.utils.CursorUtils;
import com.lcl.poolchat.common.common.utils.RedisUtils;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Set;

/**
 * Description: 全局房间
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-07-23
 */
@Component
public class HotRoomCache {

    /**
     * 获取热门群聊翻页
     * 根据游标获取
     */
    public CursorPageBaseResp<Pair<Long, Double>> getRoomCursorPage(CursorPageBaseReq pageBaseReq) {
        return CursorUtils.getCursorPageByRedis(pageBaseReq, RedisKey.getKey(RedisKey.HOT_ROOM_ZET), Long::parseLong);
    }

    /**
     * 根据更新时间获取热门房间id
     */
    public Set<ZSetOperations.TypedTuple<String>> getRoomRange(Double hotStart, Double hotEnd) {
        return RedisUtils.zRangeByScoreWithScores(RedisKey.getKey(RedisKey.HOT_ROOM_ZET), hotStart, hotEnd);
    }

    /**
     * 更新热门群聊的最新时间
     */
    public void refreshActiveTime(Long roomId, Date refreshTime) {
        RedisUtils.zAdd(RedisKey.getKey(RedisKey.HOT_ROOM_ZET), roomId, (double) refreshTime.getTime());
    }
}
