package com.ln.tms.enums;

/**
 * TookType - 签收超时原因
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public enum SignType implements BaseEnum {
    jt(0, "不可抗力：交通原因"),
    tq(1, "不可抗力：天气原因"),
    xx(2, "客户原因：单位件节假日休息"),
    js(3, "客户原因：拒收"),
    jj(4, "客户原因：商家要求截件"),
    lx(5, "客户原因：无法联系"),
    xgm(6, "客户原因：修改信息"),
    gq(7, "客户原因：要求改期派送"),
    zq(8, "客户原因：要求自取"),
    zj(9, "快递原因：超区转件"),
    zt(10, "快递原因：超区自提"),
    psyc(11, "快递原因：当地配送延迟"),
    yw(12, "快递原因：到达目的地当地延误"),
    py(13, "快递原因：地址偏远"),
    flcc(14, "快递原因：分拣差错"),
    ps(15, "快递原因：破损"),
    wlqs(16, "快递原因：未录签收"),
    ys(17, "快递原因：遗失");


    private final int code;
    private final String desc;

    SignType(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public static SignType codeOf(int code) {
        for (SignType tookType : SignType.values()) {
            if (tookType.getCode() == code) {
                return tookType;
            }
        }
        return jt;
    }

    public static SignType descOf(String desc) {
        for (SignType type : SignType.values()) {
            if (type.getDesc().equals(desc)) {
                return type;
            }
        }
        return jt;
    }

    public static SignType checkDesc(String desc) {
        for (SignType type : SignType.values()) {
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
