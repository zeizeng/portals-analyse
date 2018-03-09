package personal.weizeng.portals.dto.tieba;

import java.util.Calendar;
import java.util.UUID;

/**
 * Created by Administrator on 2018/3/8.
 */
public class TiebaDto {

    private String UUId = UUID.randomUUID().toString().replaceAll("-", "");

    private String name;

    private String superCategory;

    private String lowCategory;

    private String url;

    private int foucs;

    private int postTotal;

    private int postSuperior;

    private int picNum;

    private int groups;

    private int groupMenber;

    private long date = Calendar.getInstance().getTimeInMillis();


    public String getUUId() {
        return UUId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSuperCategory() {
        return superCategory;
    }

    public void setSuperCategory(String superCategory) {
        this.superCategory = superCategory;
    }

    public String getLowCategory() {
        return lowCategory;
    }

    public void setLowCategory(String lowCategory) {
        this.lowCategory = lowCategory;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getFoucs() {
        return foucs;
    }

    public void setFoucs(int foucs) {
        this.foucs = foucs;
    }

    public int getPostTotal() {
        return postTotal;
    }

    public void setPostTotal(int postTotal) {
        this.postTotal = postTotal;
    }

    public int getPostSuperior() {
        return postSuperior;
    }

    public void setPostSuperior(int postSuperior) {
        this.postSuperior = postSuperior;
    }

    public int getPicNum() {
        return picNum;
    }

    public void setPicNum(int picNum) {
        this.picNum = picNum;
    }

    public int getGroups() {
        return groups;
    }

    public void setGroups(int groups) {
        this.groups = groups;
    }

    public int getGroupMenber() {
        return groupMenber;
    }

    public void setGroupMenber(int groupMenber) {
        this.groupMenber = groupMenber;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
