package com.ln.tms.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * StateType - 物流状态
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public enum StateType implements BaseEnum {

    DSC(0, "待揽件"),
    YLS(1, "已揽收或已收件"),
    ZTZ(2, "在途中"),
    DPJ(201, "到达派件城市"),
    PJZ(202, "派件中"),
    FYZ(203, "已到达合作点/已放入快递柜/自提柜/驿站代收"),
    YQS(3, "已签收"),
    QCY(311, "已取出快递柜或驿站"),
    WTJ(4, "问题件"),
    WXX(401, "发货无信息"),
    WQS(402, "超时未签收"),
    WGX(403, "超时未更新"),
    JTJ(404, "拒收或退件"),
    CWQ(412, "快递柜或驿站超时未取");

    private final int code;

    private final String describe;

    public String getDescribe() {
        return describe;
    }

    StateType(int code, String describe) {
        this.code = code;
        this.describe = describe;
    }

    public static StateType codeOf(int code) {
        for (StateType e : StateType.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return DSC;
    }

    public static StateType describeOf(String describe) {
        for (StateType e : StateType.values()) {
            if (e.describe.equals(describe)) {
                return e;
            }
        }
        return DSC;
    }

    @Override
    @JsonValue
    public int code() {
        return code;
    }
}
