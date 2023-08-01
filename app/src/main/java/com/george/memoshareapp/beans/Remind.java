package com.george.memoshareapp.beans;

public class Remind {
    private String reminderUserPhoneNumber;     //提醒人账号
    private String remindedUserPhoneNumber;     //被提醒人账号
    private String remindContent;               //提醒内容
    private String remindTime;                  //提醒时间
    private String remindInterval;              //提醒间隔
    private String remindNote;                  //备注
    private String selectDate;

    public String getReminderUserPhoneNumber() {
        return reminderUserPhoneNumber;
    }

    public void setReminderUserPhoneNumber(String reminderUserPhoneNumber) {
        this.reminderUserPhoneNumber = reminderUserPhoneNumber;
    }

    public String getRemindedUserPhoneNumber() {
        return remindedUserPhoneNumber;
    }

    public void setRemindedUserPhoneNumber(String remindedUserPhoneNumber) {
        this.remindedUserPhoneNumber = remindedUserPhoneNumber;
    }

    public String getRemindContent() {
        return remindContent;
    }

    public void setRemindContent(String remindContent) {
        this.remindContent = remindContent;
    }

    public String getRemindTime() {
        return remindTime;
    }

    public void setRemindTime(String remindTime) {
        this.remindTime = remindTime;
    }

    public String getRemindInterval() {
        return remindInterval;
    }

    public void setRemindInterval(String remindInterval) {
        this.remindInterval = remindInterval;
    }

    public String getRemindNote() {
        return remindNote;
    }

    public void setRemindNote(String remindNote) {
        this.remindNote = remindNote;
    }

    public String getSelectDate() {
        return selectDate;
    }

    public void setSelectDate(String selectDate) {
        this.selectDate = selectDate;
    }

    @Override
    public String toString() {
        return "Remind{" +
                "reminderUserPhoneNumber='" + reminderUserPhoneNumber + '\'' +
                ", remindedUserPhoneNumber='" + remindedUserPhoneNumber + '\'' +
                ", remindContent='" + remindContent + '\'' +
                ", remindTime='" + remindTime + '\'' +
                ", remindInterval='" + remindInterval + '\'' +
                ", remindNote='" + remindNote + '\'' +
                '}';
    }
}
