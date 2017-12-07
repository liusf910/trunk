package com.ln.tms.enums;

/**
 * TookType - 配送状态
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public enum OrderType implements BaseEnum {
    qs(0, "签收"),
    thqs(1, "退回签收"),
    thz(2, "退回中"),
    ys(3, "遗失");


    private final int code;
    private final String desc;

    OrderType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static OrderType codeOf(int code) {
        for (OrderType tookType : OrderType.values()) {
            if (tookType.getCode() == code) {
                return tookType;
            }
        }
        return qs;
    }

    public static OrderType descOf(String desc) {
        for (OrderType type : OrderType.values()) {
            if (type.getDesc().equals(desc)) {
                return type;
            }
        }
        return qs;
    }

    public static OrderType checkDesc(String desc) {
        for (OrderType type : OrderType.values()) {
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
