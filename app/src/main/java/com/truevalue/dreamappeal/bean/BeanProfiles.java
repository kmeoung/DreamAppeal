package com.truevalue.dreamappeal.bean;

public class BeanProfiles {
    private int fieldCount;
    private int affectedRows;
    private int insertId;
    private String info;
    private int serverStatus;
    private int warningStatus;

    public BeanProfiles(int fieldCount, int affectedRows, int insertId, String info, int serverStatus, int warningStatus) {
        this.fieldCount = fieldCount;
        this.affectedRows = affectedRows;
        this.insertId = insertId;
        this.info = info;
        this.serverStatus = serverStatus;
        this.warningStatus = warningStatus;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public void setFieldCount(int fieldCount) {
        this.fieldCount = fieldCount;
    }

    public int getAffectedRows() {
        return affectedRows;
    }

    public void setAffectedRows(int affectedRows) {
        this.affectedRows = affectedRows;
    }

    public int getInsertId() {
        return insertId;
    }

    public void setInsertId(int insertId) {
        this.insertId = insertId;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getServerStatus() {
        return serverStatus;
    }

    public void setServerStatus(int serverStatus) {
        this.serverStatus = serverStatus;
    }

    public int getWarningStatus() {
        return warningStatus;
    }

    public void setWarningStatus(int warningStatus) {
        this.warningStatus = warningStatus;
    }
}
