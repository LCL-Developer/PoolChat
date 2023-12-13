package com.lcl.poolchat.common.chatai.service;

import com.lcl.poolchat.common.chat.domain.entity.Message;

public interface IChatAIService {

    void chat(Message message);
}
