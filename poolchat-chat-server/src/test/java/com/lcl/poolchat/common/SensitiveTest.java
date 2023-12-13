package com.lcl.poolchat.common;

import com.lcl.poolchat.common.common.utils.sensitiveWord.ACFilter;
import com.lcl.poolchat.common.common.utils.sensitiveWord.DFAFilter;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Description:
 * Author: <a href="https://github.com/LCL-Developer">lcl</a>
 * Date: 2023-10-08
 */
public class SensitiveTest {
    @Test
    public void DFA() {
        List<String> sensitiveList = Arrays.asList("abcd", "abcbba", "adabca");
        DFAFilter instance = DFAFilter.getInstance();
        instance.loadWord(sensitiveList);
        System.out.println(instance.hasSensitiveWord("adabcd"));
    }

    @Test
    public void AC() {
        List<String> sensitiveList = Arrays.asList("abcd", "abcbba", "adabca");
        ACFilter instance = new ACFilter();
        instance.loadWord(sensitiveList);
        instance.hasSensitiveWord("adabcd");
    }

    @Test
    public void DFAMulti() {
        List<String> sensitiveList = Arrays.asList("白痴", "你是白痴", "白痴吗");
        DFAFilter instance = DFAFilter.getInstance();
        instance.loadWord(sensitiveList);
        System.out.println(instance.filter("你是白痴吗"));
    }

    @Test
    public void ACMulti() {
        List<String> sensitiveList = Arrays.asList("白痴", "你是白痴", "白痴吗");
        ACFilter instance = new ACFilter();
        instance.loadWord(sensitiveList);
        System.out.println(instance.filter("你是白痴吗"));
    }
}
