package com.ln.tms.enums;

/**
 * JobType - 任务类型
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public enum JobType implements BaseEnum {
    qxz(0, "----请选择-----"),
    took_sync(1, "首页揽收及时率Job"),
    erp_sync(2, "ERP同步数据Job"),
    bsht_sync(3, "百世汇通同步Job"),
    ems_sync(4, "EMS同步Job"),
    jd_sync(5, "京东同步Job"),
    st_sync(6, "申通同步Job"),
    yt_sync(7, "圆通同步Job");


    private final int code;
    private final String desc;

    JobType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static JobType codeOf(int code) {
        for (JobType tookType : JobType.values()) {
            if (tookType.getCode() == code) {
                return tookType;
            }
        }
        return qxz;
    }

    public static JobType descOf(String desc) {
        for (JobType type : JobType.values()) {
            if (type.getDesc().equals(desc)) {
                return type;
            }
        }
        return qxz;
    }

    public static JobType checkDesc(String desc) {
        for (JobType type : JobType.values()) {
            if (type.getDesc().equals(desc)) {
                return type;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }

    @Override
    public int code() {
        return code;
    }
}
