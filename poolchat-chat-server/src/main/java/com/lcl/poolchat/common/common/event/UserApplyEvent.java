package com.lcl.poolchat.common.common.event;

import com.lcl.poolchat.common.user.domain.entity.UserApply;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class UserApplyEvent extends ApplicationEvent {
    private UserApply userApply;

    public UserApplyEvent(Object source, UserApply userApply) {
        super(source);
        this.userApply = userApply;
    }

}
