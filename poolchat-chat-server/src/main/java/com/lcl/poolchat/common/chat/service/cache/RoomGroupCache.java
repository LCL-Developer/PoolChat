package com.lcl.poolchat.common.chat.service.cache;

import com.lcl.poolchat.common.chat.dao.RoomGroupDao;
import com.lcl.poolchat.common.chat.domain.entity.RoomGroup;
import com.lcl.poolchat.common.common.constant.RedisKey;
import com.lcl.poolchat.common.common.service.cache.AbstractRedisStringCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Description: 群组基本信息的缓存
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-06-10
 */
@Component
public class RoomGroupCache extends AbstractRedisStringCache<Long, RoomGroup> {
    @Autowired
    private RoomGroupDao roomGroupDao;

    @Override
    protected String getKey(Long roomId) {
        return RedisKey.getKey(RedisKey.GROUP_INFO_STRING, roomId);
    }

    @Override
    protected Long getExpireSeconds() {
        return 5 * 60L;
    }

    @Override
    protected Map<Long, RoomGroup> load(List<Long> roomIds) {
        List<RoomGroup> roomGroups = roomGroupDao.listByRoomIds(roomIds);
        return roomGroups.stream().collect(Collectors.toMap(RoomGroup::getRoomId, Function.identity()));
    }
}
