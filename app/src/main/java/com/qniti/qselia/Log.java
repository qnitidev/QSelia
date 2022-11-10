package com.qniti.qselia;

public class Log {


    private String staff_logID;
    private String logDate;
    private String logTime;
    private String userName;
    private String userID;
    private String rating;
    private String remark;


    public Log(String staff_logID, String logDate, String logTime, String userName,String userID, String rating,String remark) {

        this.staff_logID = staff_logID;
        this.logDate = logDate;
        this.logTime = logTime;
        this.userName = userName;
        this.userID = userID;
        this.rating = rating;
        this.remark = remark;


    }

    public String getStaff_logID() {
        return staff_logID;
    }

    public String getLogDate() {
        return logDate;
    }

    public String getLogTime() {
        return logTime;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserID() {
        return userID;
    }
    public String getRating() {
        return rating;
    }

    public String getRemark() {
        return remark;
    }
}