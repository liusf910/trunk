package com.ln.tms.enums;

/**
 * TookType - 揽件超时原因
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public enum TookType implements BaseEnum {
    skcs(0, "仓库交接超时"),
    xt(1, "系统问题无法扫描"),
    lz(2, "快递原因漏做揽收"),
    yc(3, "快递原因延迟揽收"),
    ys(4, "快递原因遗失"),
    kfj(5, "客服截件"),
    zkd(6, "转其他快递"),
    zwl(7, "转物流");

    private final int code;
    private final String desc;

    TookType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static TookType codeOf(int code) {
        for (TookType tookType : TookType.values()) {
            if (tookType.getCode() == code) {
                return tookType;
            }
        }
        return skcs;
    }

    public static TookType descOf(String desc) {
        for (TookType type : TookType.values()) {
            if (type.getDesc().equals(desc)) {
                return type;
            }
        }
        return skcs;
    }

    public static TookType checkDesc(String desc) {
        for (TookType type : TookType.values()) {
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
