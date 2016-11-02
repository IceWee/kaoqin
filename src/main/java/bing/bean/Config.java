package bing.bean;

import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 * 配置
 *
 * @author IceWee
 */
public class Config {

    private String ondutyTime;
    private String offdutyTime;
    private String excludeName;
    private Set<String> excludeNames;
    private int cardNameColumn;
    private int cardAttendateColumn;
    private int cardOndutyColumn;
    private int cardOffdutyColumn;
    private int cardDataBeginRow;
    private int attendDataBeginRow;
    private int attendNameColumn;
    private int attendDaysColumn;
    private int attendRemarkColumn;

    public Config() {
        super();
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

    public String getExcludeName() {
        return excludeName;
    }

    public void setExcludeName(String excludeName) {
        this.excludeName = excludeName;
    }

    public Set<String> getExcludeNames() {
        if (StringUtils.isBlank(this.excludeName)) {
            this.excludeNames = new HashSet<>();
        } else {
            this.excludeNames = new HashSet<>();
            String[] excludeNameArray = StringUtils.split(this.excludeName, "+");
            for (String excName : excludeNameArray) {
                if (StringUtils.isNotBlank(excName)) {
                    this.excludeNames.add(excName);
                }
            }
        }
        return excludeNames;
    }

    public void setExcludeNames(Set<String> excludeNames) {
        this.excludeNames = excludeNames;
    }

    public int getCardNameColumn() {
        return cardNameColumn;
    }

    public void setCardNameColumn(int cardNameColumn) {
        this.cardNameColumn = cardNameColumn;
    }

    public int getCardAttendateColumn() {
        return cardAttendateColumn;
    }

    public void setCardAttendateColumn(int cardAttendateColumn) {
        this.cardAttendateColumn = cardAttendateColumn;
    }

    public int getCardOndutyColumn() {
        return cardOndutyColumn;
    }

    public void setCardOndutyColumn(int cardOndutyColumn) {
        this.cardOndutyColumn = cardOndutyColumn;
    }

    public int getCardOffdutyColumn() {
        return cardOffdutyColumn;
    }

    public void setCardOffdutyColumn(int cardOffdutyColumn) {
        this.cardOffdutyColumn = cardOffdutyColumn;
    }

    public int getCardDataBeginRow() {
        return cardDataBeginRow;
    }

    public void setCardDataBeginRow(int cardDataBeginRow) {
        this.cardDataBeginRow = cardDataBeginRow;
    }

    public int getAttendDataBeginRow() {
        return attendDataBeginRow;
    }

    public void setAttendDataBeginRow(int attendDataBeginRow) {
        this.attendDataBeginRow = attendDataBeginRow;
    }

    public int getAttendNameColumn() {
        return attendNameColumn;
    }

    public void setAttendNameColumn(int attendNameColumn) {
        this.attendNameColumn = attendNameColumn;
    }

    public int getAttendRemarkColumn() {
        return attendRemarkColumn;
    }

    public void setAttendRemarkColumn(int attendRemarkColumn) {
        this.attendRemarkColumn = attendRemarkColumn;
    }

    public int getAttendDaysColumn() {
        return attendDaysColumn;
    }

    public void setAttendDaysColumn(int attendDaysColumn) {
        this.attendDaysColumn = attendDaysColumn;
    }

    @Override
    public String toString() {
        return "Config{" + "ondutyTime=" + ondutyTime + ", offdutyTime=" + offdutyTime + ", excludeName=" + excludeName + ", excludeNames=" + excludeNames + ", cardNameColumn=" + cardNameColumn + ", cardAttendateColumn=" + cardAttendateColumn + ", cardOndutyColumn=" + cardOndutyColumn + ", cardOffdutyColumn=" + cardOffdutyColumn + ", cardDataBeginRow=" + cardDataBeginRow + ", attendDataBeginRow=" + attendDataBeginRow + ", attendNameColumn=" + attendNameColumn + ", attendDaysColumn=" + attendDaysColumn + ", attendRemarkColumn=" + attendRemarkColumn + '}';
    }

}
