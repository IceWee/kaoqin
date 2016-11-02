package bing.bean;

/**
 * 打卡记录
 *
 * @author IceWee
 */
public class CardRecord {
    
    private String username;
    private String attendate;
    private String ondutyTime;
    private String offdutyTime;
    private int code;
    
    public CardRecord() {
        super();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAttendate() {
        return attendate;
    }

    public void setAttendate(String attendate) {
        this.attendate = attendate;
    }

    public String getOndutyTime() {
        return ondutyTime;
    }

    public void setOndutyTime(String ondutyTime) {
        this.ondutyTime = ondutyTime;
    }

    public String getOffdutyTime() {
        return offdutyTime;
    }

    public void setOffdutyTime(String offdutyTime) {
        this.offdutyTime = offdutyTime;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "CardRecord{" + "username=" + username + ", attendate=" + attendate + ", ondutyTime=" + ondutyTime + ", offdutyTime=" + offdutyTime + ", code=" + code + '}';
    }
    
}
