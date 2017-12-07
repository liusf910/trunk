package com.ln.tms.sync;

import java.util.concurrent.Callable;

/**
 * ICallable - 回调基接口
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface ICallable<T> extends Callable<T> {

    @Override
    T call() throws Exception;
}
