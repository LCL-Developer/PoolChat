package com.lcl.poolchat.common.chat.service.strategy.msg;

import com.lcl.poolchat.common.common.exception.CommonErrorEnum;
import com.lcl.poolchat.common.common.utils.AssertUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Description:
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-06-04
 */
public class MsgHandlerFactory {
    private static final Map<Integer, AbstractMsgHandler> STRATEGY_MAP = new HashMap<>();

    public static void register(Integer code, AbstractMsgHandler strategy) {
        STRATEGY_MAP.put(code, strategy);
    }

    public static AbstractMsgHandler getStrategyNoNull(Integer code) {
        AbstractMsgHandler strategy = STRATEGY_MAP.get(code);
        AssertUtil.isNotEmpty(strategy, CommonErrorEnum.PARAM_VALID);
        return strategy;
    }
}
