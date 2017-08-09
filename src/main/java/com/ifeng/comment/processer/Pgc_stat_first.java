package com.ifeng.comment.processer;

import com.alibaba.fastjson.JSON;
import com.ifeng.comment.bean.LogInfo;
import com.ifeng.comment.bean.VdnInfo;
import com.ifeng.comment.kafka.SplitFileInput;

import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duanyb on 2017/7/10.
 */
public class Pgc_stat_first implements SplitFileInput {
    @Override
    public String splitLine(String sline) {
        try {
            LogInfo logInfo = JSON.parseObject(sline, LogInfo.class);
            return logInfo.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
