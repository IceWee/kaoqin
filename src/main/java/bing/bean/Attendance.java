package bing.bean;

/**
 * 考勤信息
 *
 * @author IceWee
 */
public class Attendance {

    /**
     * 人员名称
     */
    private String name;

    /**
     * 出勤天数
     */
    private String dutyDays;

    /**
     * 备注
     */
    private String remark;

    public Attendance() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDutyDays() {
        return dutyDays;
    }

    public void setDutyDays(String dutyDays) {
        this.dutyDays = dutyDays;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return  "[" + name + "] 实出勤 " + dutyDays + " 天，异常日期：" + remark;
    }

}
