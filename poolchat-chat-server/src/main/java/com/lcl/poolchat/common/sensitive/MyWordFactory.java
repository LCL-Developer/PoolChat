package com.lcl.poolchat.common.sensitive;

import com.lcl.poolchat.common.common.utils.sensitiveWord.IWordFactory;
import com.lcl.poolchat.common.sensitive.dao.SensitiveWordDao;
import com.lcl.poolchat.common.sensitive.domain.SensitiveWord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MyWordFactory implements IWordFactory {
    @Autowired
    private SensitiveWordDao sensitiveWordDao;

    @Override
    public List<String> getWordList() {
        return sensitiveWordDao.list()
                .stream()
                .map(SensitiveWord::getWord)
                .collect(Collectors.toList());
    }
}
