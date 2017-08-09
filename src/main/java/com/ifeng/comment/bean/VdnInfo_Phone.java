package com.ifeng.comment.bean;


import java.io.Serializable;

/**
 * Created by duanyb on 2016/10/18.
 */
public class VdnInfo_Phone implements Serializable {

    private static final long serialVersionUID =  -395860770678584L;

    private String tr = "";
    private String tm = "";
    private String ip = "";
    private String uid = "";
    private String frm = "";
    private String url = "";
    private String err = "";
    private String ptype = "";
    private String guid = "";
    private String sdkv = "";
    private String os = "";
    private String channel = "";
    private String appv = "";
    private String statistype = "vv";
    private String city = "";
    private String netName = "";
    private String from = "";
    private String type ="";  //标记pc /phone

    @Override
    public int hashCode() {
        return this.tm.hashCode() * 17
                + this.tr.hashCode() * 17
                + this.frm.hashCode() * 17
                + this.ip.hashCode() * 17
                + this.url.hashCode() * 17
                + this.uid.hashCode() * 17
                + this.ptype.hashCode() * 17
                + this.guid.hashCode() * 17
                + this.os.hashCode() * 17
                + this.channel.hashCode() * 17
                + this.appv.hashCode() * 17
                + this.sdkv.hashCode() * 17
                + this.statistype.hashCode() * 17;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof VdnInfo_Phone)) return false;
        VdnInfo_Phone en = (VdnInfo_Phone) obj;
        return en.tr.equals(this.tr) && en.ptype.equals(this.ptype)
                && en.ip.equals(this.ip) && en.url.equals(this.url)
                && en.uid.equals(this.uid)
                && en.frm.equals(this.frm) && en.tm.equals(this.tm)
                && en.guid.equals(this.guid) && en.os.equals(this.os)
                && en.channel.equals(this.channel)
                && en.appv.equals(this.appv)
                && en.sdkv.equals(this.sdkv)
                && en.statistype.equals(this.statistype);
    }

    @Override
    public VdnInfo_Phone clone() {
        VdnInfo_Phone en = new VdnInfo_Phone();
        en.setIp(this.getIp());
        en.setUid(this.getUid());
        en.setFrm(this.getFrm());
        en.setErr(this.getErr());
        en.setUrl(this.getUrl());
        en.setPtype(this.getPtype());
        en.setGuid(this.getGuid());
        en.setOs(this.getOs());
        en.setChannel(this.getChannel());
        en.setStatistype(this.getStatistype());
        en.setAppv(this.getAppv());
        en.setSdkv(this.getSdkv());
        en.setCity(this.getCity());
        en.setNetName(this.getNetName());
        en.setFrom(this.getFrom());
//        try {
//            Date data2 = new SimpleDateFormat("yyyy-MM-dd HHmm").parse(this.getTm() + " " + this.getTr());
//            Calendar c = Calendar.getInstance();
//            c.setTime(data2);
//            c.add(Calendar.MINUTE,-6);
//            String time = new SimpleDateFormat("yyyy-MM-dd HHmm").format(c.getTime());
//            en.setTm(time.split("\\s+")[0]);
//            en.setTr(time.split("\\s+")[1]);
//        } catch (ParseException e) {
//            en.setTm(this.getTm());
//            en.setTr(this.getTr());
//        }
        en.setTm(this.getTm());
        en.setTr("");
        return en;
    }

    public VdnInfo_Phone clone_() {
        VdnInfo_Phone en = new VdnInfo_Phone();
        en.setIp(this.getIp());
        en.setUid(this.getUid());
        en.setFrm(this.getFrm());
        en.setUrl(this.getUrl());
        en.setPtype(this.getPtype());
        en.setGuid(this.getGuid());
        en.setTm(this.getTm());
        en.setTr(this.getTr());
        en.setOs(this.getOs());
        en.setChannel(this.getChannel());
        en.setStatistype(this.getStatistype());
        en.setAppv(this.getAppv());
        en.setSdkv(this.getSdkv());
        en.setCity(this.getCity());
        en.setNetName(this.getNetName());
        en.setFrom(this.getFrom());
        en.setErr("");
        en.setType(this.getType());
        return en;
    }

    public VdnInfo_Phone clone_2() {
        VdnInfo_Phone en = new VdnInfo_Phone();
        en.setIp(this.getIp());
        en.setUid(this.getUid());
        en.setFrm(this.getFrm());
        en.setUrl(this.getUrl());
        en.setPtype(this.getPtype());
        en.setGuid(this.getGuid());
        en.setTm(this.getTm());
        en.setTr(this.getTr());
        en.setOs(this.getOs());
        en.setChannel(this.getChannel());
        en.setStatistype(this.getStatistype());
        en.setAppv(this.getAppv());
        en.setSdkv(this.getSdkv());
        en.setErr(this.getErr());
        en.setCity(this.getCity());
        en.setNetName(this.getNetName());
        en.setFrom(this.getFrom());
        return en;
    }

    public VdnInfo_Phone clone_1() {
        VdnInfo_Phone en = new VdnInfo_Phone();
        en.setUid(this.getUid());
        en.setPtype(this.getPtype());
        en.setUrl(this.getUrl());
        en.setIp(this.getIp());
        en.setErr(this.getErr());
        en.setTm(this.getTm());
        en.setTr(this.getTr());
        return en;
    }

    public String getStatistype() {
        return statistype;
    }

    public void setStatistype(String statistype) {
        this.statistype = statistype;
    }

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTr() {
        return tr;
    }

    public void setTr(String tr) {
        this.tr = tr;
    }

    public String getTm() {
        return tm;
    }

    public void setTm(String tm) {
        this.tm = tm;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFrm() {
        return frm;
    }

    public void setFrm(String frm) {
        this.frm = frm;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErr() {
        return err;
    }

    public void setErr(String err) {
        this.err = err;
    }

    public String getPtype() {
        return ptype;
    }

    public void setPtype(String ptype) {
        this.ptype = ptype;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getAppv() {
        return appv;
    }

    public void setAppv(String appv) {
        this.appv = appv;
    }

    public String getSdkv() {
        return sdkv;
    }

    public void setSdkv(String sdkv) {
        this.sdkv = sdkv;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNetName() {
        return netName;
    }

    public void setNetName(String netName) {
        this.netName = netName;
    }

    @Override
    public String toString() {
        return new StringBuilder().append(tr).append("\t").append(tm).append("\t").append(ip).append("\t").append(uid).append("\t").append(frm).
                append("\t").append(url).
                append("\t").append(err)
                .append("\t").append(ptype)
                .append("\t").append(guid)
                .append("\t").append(sdkv)
                .append("\t").append(os)
                .append("\t").append(channel)
                .append("\t").append(appv)
                //.append("\t").append(statistype)
                //.append("\t").append(city)
                //.append("\t").append(netName)
                .toString();
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
