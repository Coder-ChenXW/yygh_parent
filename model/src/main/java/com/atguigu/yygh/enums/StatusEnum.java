package com.atguigu.yygh.enums;

public enum StatusEnum {

    LOCK(0, "锁定"),
    NORMAL(1, "正常");

    private Integer status;
    private String statusString;


    public static String getStatusStringByStatus(Integer status) {
        StatusEnum[] values = StatusEnum.values();
        for (StatusEnum value : values) {
            if (value.getStatus().intValue()==status.intValue()) {
                return value.getStatusString();
            }
        }
        return "";
    }


    StatusEnum() {
    }

    StatusEnum(Integer status, String statusString) {
        this.status = status;
        this.statusString = statusString;
    }

    /**
     * 获取
     * @return status
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置
     * @param status
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取
     * @return statusString
     */
    public String getStatusString() {
        return statusString;
    }

    /**
     * 设置
     * @param statusString
     */
    public void setStatusString(String statusString) {
        this.statusString = statusString;
    }

    public String toString() {
        return "StatusEnum{status = " + status + ", statusString = " + statusString + "}";
    }
}
