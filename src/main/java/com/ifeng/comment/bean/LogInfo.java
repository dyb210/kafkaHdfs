package com.ifeng.comment.bean;



import java.io.Serializable;

/**
 * Created by duanyb on 2017/4/10.
 */
public class LogInfo implements Serializable {
    private static final long serialVersionUID = -37408676788590101L;

    private long pv;
    private long ev;
    private long comments;
    private long store;
    private long share;
    private long subscribe;
    private long cancelSubscribe;


    private long lastModifyTime;
    private String createDate;
    private String createTime;
    private String pgcId;
    private String mediaId;
    private String type;
    private String channel;
    private String weMediaId;
    private String uid;


    public LogInfo() {
        this.subscribe = 0L;
        this.cancelSubscribe = 0L;
        this.pv = 0L;
        this.ev = 0L;
        this.comments = 0L;
        this.store = 0L;
        this.share = 0L;
        this.createTime = "";
        this.createDate = "";
        this.pgcId = "";
        this.mediaId = "";
        this.type = "";
        this.channel = "";
        this.weMediaId = "";
        this.lastModifyTime = 0L;
        this.uid = "";
    }

    @Override
    public String toString() {
        return new StringBuilder().append(pv)
                .append("\t").append(ev)
                .append("\t").append(comments)
                .append("\t").append(store)
                .append("\t").append(share).
                 append("\t").append(subscribe).
                 append("\t").append(cancelSubscribe)
                .append("\t").append(lastModifyTime)
                .append("\t").append(createDate)
                .append("\t").append(createTime)
                .append("\t").append(pgcId)
                .append("\t").append(mediaId)
                .append("\t").append(type)
                .append("\t").append(channel)
                .append("\t").append(weMediaId)
                .append("\t").append(uid)
                .toString();
    }

    public long getComments() {
        return comments;
    }

    public void setComments(long comments) {
        this.comments = comments;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }


    public long getPv() {
        return pv;
    }

    public void setPv(long pv) {
        this.pv = pv;
    }


    public String getPgcId() {
        return pgcId;
    }

    public void setPgcId(String pgcId) {
        this.pgcId = pgcId;
    }

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    @Override
    public int hashCode() {
        return this.createDate.hashCode() * 26
                        + this.createTime.hashCode() * 25
                        + this.pgcId.hashCode() * 24
                        + this.mediaId.hashCode() * 23
                        + this.channel.hashCode() * 22
                        + this.type.hashCode() * 21;
//                + this.statsType.hashCode() * 31
//                + this.refer.hashCode() * 30
//                + this.url.hashCode() * 29;
//                + this.deviceDesc.hashCode() * 28
//                + this.ci.hashCode() * 27
//                +this.uid.hashCode() * 24;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof LogInfo)) {
            return false;
        }
        LogInfo en = (LogInfo) obj;

        return en.pgcId.equals(this.pgcId)
                && en.mediaId.equals(this.mediaId)
                && en.createDate.equals(this.createDate)
                && en.createTime.equals(this.createTime)
                && en.type.equals(this.type)
                && en.channel.equals(this.channel);
    }

    @Override
    public LogInfo clone(){
        LogInfo logInfo = new LogInfo();
        logInfo.setCreateTime(this.getCreateTime());
        logInfo.setLastModifyTime(this.getLastModifyTime());
        logInfo.setStore(this.getStore());
        logInfo.setCreateDate(this.getCreateDate());
        logInfo.setPgcId(this.getPgcId());
        logInfo.setShare(this.getShare());
        logInfo.setCancelSubscribe(this.getCancelSubscribe());
        logInfo.setChannel(this.getChannel());
        logInfo.setComments(this.getComments());
        logInfo.setEv(this.getEv());
        logInfo.setMediaId(this.getMediaId());
        logInfo.setPv(this.getPv());
        logInfo.setSubscribe(this.getSubscribe());
        logInfo.setType(this.getType());
        logInfo.setWeMediaId(this.getWeMediaId());
        logInfo.setUid(this.getUid());
        return logInfo;
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public long getEv() {
        return ev;
    }

    public void setEv(long ev) {
        this.ev = ev;
    }

    public long getStore() {
        return store;
    }

    public void setStore(long store) {
        this.store = store;
    }

    public long getShare() {
        return share;
    }

    public void setShare(long share) {
        this.share = share;
    }



    public String getWeMediaId() {
        return weMediaId;
    }

    public void setWeMediaId(String weMediaId) {
        this.weMediaId = weMediaId;
    }

    public long getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(long subscribe) {
        this.subscribe = subscribe;
    }

    public long getCancelSubscribe() {
        return cancelSubscribe;
    }

    public void setCancelSubscribe(long cancelSubscribe) {
        this.cancelSubscribe = cancelSubscribe;
    }

    public long getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(long lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }


}

