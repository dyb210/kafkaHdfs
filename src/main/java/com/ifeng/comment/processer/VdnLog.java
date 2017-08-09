package com.ifeng.comment.processer;

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
public class VdnLog implements SplitFileInput {


    private final Pattern pattern_uuid = Pattern.compile("uuid=[\\d|\\w]+");
    private final Pattern patternrexp_vid = Pattern.compile("vid=[\\d|\\w]+");
    private final Pattern patternrexp_uid = Pattern.compile("uid=[\\d|\\w]+");
    private final Pattern patternrexp_from = Pattern.compile("from=[\\d|\\w]+");
    private final Pattern patternrexp_systemInfo = Pattern.compile("systemInfo=[\\d|\\w|\\s]+");
    private final Pattern patternrexp_url = Pattern.compile("url=((ht|f)tps?|htp)://[\\w\\-]+(\\.[\\w\\-]+)+([\\w\\-.,@?^=%&:/~+#]*[\\w\\-@?^=%&/~+#])?$");
    private final Pattern patternrexp_errid = Pattern.compile("err=[\\d|\\w]+");
    private final Pattern patternrexp_gid = Pattern.compile("gid=[\\d|\\w]+");
    private final Pattern patternrexp_tm = Pattern.compile("tm=[\\d|\\w]+");

    @Override
    public String splitLine(String sline) {
        if (sline.contains("vplayer.js")) {
            Matcher matcher_vid = patternrexp_vid.matcher(sline);
            if (matcher_vid.find()) {
                String vid = matcher_vid.group();
                if (vid.contains("android") || vid.contains("ijk") || vid.contains("ifeng")) {

                } else {
                    try {
                        VdnInfo vdnInfo = new VdnInfo();
                        vdnInfo.setType("pc");
                        if (sline.contains("vplayer.js")) {
                            String tmp[] = sline.split(",");
                            String se[] = tmp[3].split("\\s+");
                            String time = tmp[2].split("\\s+")[0].substring(1);
                            String sessionstr = se[1];
                            String sessionstrDecode = URLDecoder.decode(sessionstr, "UTF-8");
                            Matcher matcher_err = patternrexp_errid.matcher(sessionstrDecode);
                            if (matcher_err.find()) {
                                String errcode = matcher_err.group(0).substring(4);
                                if (errcode.contains("30400") && errcode.compareTo("304004") >= 0) {
                                    errcode = "304004";
                                } else if (errcode.contains("301030")) {
                                    errcode = "301030";
                                }
                                if (isCheck(errcode)) {
                                    String ip = tmp[0];
                                    vdnInfo.setIp(ip);
                                    vdnInfo.setErr(errcode);
                                    vdnInfo.setPtype(vid.substring(4));
                                    Matcher matcher_from = patternrexp_from.matcher(sessionstrDecode);
                                    if (matcher_from.find()) {
                                        vdnInfo.setFrom(matcher_from.group().substring(5));
                                    }
                                    Matcher matcher_gid = patternrexp_gid.matcher(sessionstrDecode);
                                    if (matcher_gid.find()) {
                                        vdnInfo.setGuid(matcher_gid.group(0).substring(4));
                                    } else {
                                        vdnInfo.setGuid("ignorant");
                                    }
                                    Matcher matcher_tm = patternrexp_tm.matcher(sessionstrDecode);
                                    if (matcher_tm.find()) {
                                        vdnInfo.setTmtmp(matcher_tm.group(0).substring(3));
                                    } else {
                                        vdnInfo.setTmtmp("others");
                                    }
                                    try {
                                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MMM/yyyy:HH:mm:ss", Locale.ENGLISH);
                                        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd HHmm");
                                        String timeformat = simpleDateFormat1.format(simpleDateFormat.parse(time));
                                        vdnInfo.setTr(timeformat.split("\\s+")[1]);
                                        vdnInfo.setTm(timeformat.split("\\s+")[0]);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    Matcher matcher_uid = patternrexp_uid.matcher(sessionstrDecode);
                                    if (matcher_uid.find()) {
                                        vdnInfo.setUid(matcher_uid.group().substring(4));
                                    } else {
                                        vdnInfo.setUid("others");
                                    }
                                    Matcher matcher_url = patternrexp_url.matcher(sessionstrDecode);
                                    if (matcher_url.find()) {
                                        vdnInfo.setUrl(matcher_url.group(0).substring(4));
                                    } else {
                                        vdnInfo.setUrl("others");
                                    }
                                    if (!vdnInfo.getErr().equals("") && !vdnInfo.getIp().equals(""))
                                        return vdnInfo.toString();
                                }
                            }
                        }
                    } catch (Exception err) {
                        err.printStackTrace();
                    } finally {
                    }
                }
            }
        }
        return null;
    }

    public boolean isCheck(String str) {
        switch (str) {
            case "208000":
                return true;
            case "304001":
                return true;
            case "304002":
                return true;
            case "304003":
                return true;
            case "304004":
                return true;
            case "301010":
                return true;
            case "301020":
                return true;
            case "301030":
                return true;
            case "100000":
                return true;
            case "303000":
                return true;
            case "110000":
                return true;
            case "301040":
                return true;
            case "601000":
                return true;
            case "602000":
                return true;
            default:
                return false;
        }

    }
}
