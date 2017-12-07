package com.ln.tms.sync;

/**
 * SyncInfoInterface - 快递信息后台更新
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface SyncInfoInterface {

    /**
     * 定时更新快递信息
     *
     * @return Integer
     */
    void syncInfo();
}
