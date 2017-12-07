package com.ln.tms.sync;

import com.google.common.util.concurrent.FutureCallback;

/**
 * ICallable - 回调基接口
 *
 * @author HuaXin Team
 * @version 1.0.0
 */
public interface IFutureCallback<T> extends FutureCallback<T> {

    @Override
    void onSuccess(T t);

    @Override
    void onFailure(Throwable throwable);
}
