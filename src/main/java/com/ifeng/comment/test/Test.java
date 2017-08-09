package com.ifeng.comment.test;

import com.ifeng.comment.util.DateUtil;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by duanyb on 2017/7/12.
 */
public class Test {
    public static void main(String[] args) {
        String path = "/dyb/dyb/pc/2017-07-12";
        Pattern pattern = Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
        Matcher math = pattern.matcher(path);
        String realPath = path;
        if (math.find()) {
            realPath = realPath.substring(0, realPath.lastIndexOf("/"));
            realPath = realPath + "/" + DateUtil.formatDate(new Date());
        }
    }
}
