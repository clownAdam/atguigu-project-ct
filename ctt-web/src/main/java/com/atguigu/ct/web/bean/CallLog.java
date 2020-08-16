package com.atguigu.ct.web.bean;

/**
 * @author clown
 */
public class CallLog {
    private Integer id;
    private Integer telId;
    private Integer dateId;
    private Integer sumCall;
    private Integer sumDuration;

    @Override
    public String toString() {
        return "CallLog{" +
                "id=" + id +
                ", telId=" + telId +
                ", dateId=" + dateId +
                ", sumCall=" + sumCall +
                ", sumDuration=" + sumDuration +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTelId() {
        return telId;
    }

    public void setTelId(Integer telId) {
        this.telId = telId;
    }

    public Integer getDateId() {
        return dateId;
    }

    public void setDateId(Integer dateId) {
        this.dateId = dateId;
    }

    public Integer getSumCall() {
        return sumCall;
    }

    public void setSumCall(Integer sumCall) {
        this.sumCall = sumCall;
    }

    public Integer getSumDuration() {
        return sumDuration;
    }

    public void setSumDuration(Integer sumDuration) {
        this.sumDuration = sumDuration;
    }
}
