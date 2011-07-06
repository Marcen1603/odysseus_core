// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.scheduler2;

public final class SchedulerListenerPrxHelper extends Ice.ObjectPrxHelperBase implements SchedulerListenerPrx
{
    public void
    onJobInsert(JobDescription ctx)
    {
        onJobInsert(ctx, null, false);
    }

    public void
    onJobInsert(JobDescription ctx, java.util.Map<String, String> __ctx)
    {
        onJobInsert(ctx, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    onJobInsert(JobDescription ctx, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __delBase = __getDelegate(false);
                _SchedulerListenerDel __del = (_SchedulerListenerDel)__delBase;
                __del.onJobInsert(ctx, __ctx);
                return;
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public void
    onJobRemoved(JobPrx ctx)
    {
        onJobRemoved(ctx, null, false);
    }

    public void
    onJobRemoved(JobPrx ctx, java.util.Map<String, String> __ctx)
    {
        onJobRemoved(ctx, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    onJobRemoved(JobPrx ctx, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __delBase = __getDelegate(false);
                _SchedulerListenerDel __del = (_SchedulerListenerDel)__delBase;
                __del.onJobRemoved(ctx, __ctx);
                return;
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public void
    onNextStep(int timestamp)
    {
        onNextStep(timestamp, null, false);
    }

    public void
    onNextStep(int timestamp, java.util.Map<String, String> __ctx)
    {
        onNextStep(timestamp, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    onNextStep(int timestamp, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __delBase = __getDelegate(false);
                _SchedulerListenerDel __del = (_SchedulerListenerDel)__delBase;
                __del.onNextStep(timestamp, __ctx);
                return;
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public void
    onPostExecute(ExecutionContext ctx)
    {
        onPostExecute(ctx, null, false);
    }

    public void
    onPostExecute(ExecutionContext ctx, java.util.Map<String, String> __ctx)
    {
        onPostExecute(ctx, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    onPostExecute(ExecutionContext ctx, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __delBase = __getDelegate(false);
                _SchedulerListenerDel __del = (_SchedulerListenerDel)__delBase;
                __del.onPostExecute(ctx, __ctx);
                return;
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public void
    onPreExecute(ExecutionContext ctx)
    {
        onPreExecute(ctx, null, false);
    }

    public void
    onPreExecute(ExecutionContext ctx, java.util.Map<String, String> __ctx)
    {
        onPreExecute(ctx, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    onPreExecute(ExecutionContext ctx, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __delBase = __getDelegate(false);
                _SchedulerListenerDel __del = (_SchedulerListenerDel)__delBase;
                __del.onPreExecute(ctx, __ctx);
                return;
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public static SchedulerListenerPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        SchedulerListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SchedulerListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::SchedulerListener"))
                {
                    SchedulerListenerPrxHelper __h = new SchedulerListenerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SchedulerListenerPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        SchedulerListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SchedulerListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::SchedulerListener", __ctx))
                {
                    SchedulerListenerPrxHelper __h = new SchedulerListenerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SchedulerListenerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SchedulerListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::SchedulerListener"))
                {
                    SchedulerListenerPrxHelper __h = new SchedulerListenerPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static SchedulerListenerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        SchedulerListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::SchedulerListener", __ctx))
                {
                    SchedulerListenerPrxHelper __h = new SchedulerListenerPrxHelper();
                    __h.__copyFrom(__bb);
                    __d = __h;
                }
            }
            catch(Ice.FacetNotExistException ex)
            {
            }
        }
        return __d;
    }

    public static SchedulerListenerPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        SchedulerListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SchedulerListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                SchedulerListenerPrxHelper __h = new SchedulerListenerPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static SchedulerListenerPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SchedulerListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            SchedulerListenerPrxHelper __h = new SchedulerListenerPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _SchedulerListenerDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _SchedulerListenerDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, SchedulerListenerPrx v)
    {
        __os.writeProxy(v);
    }

    public static SchedulerListenerPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            SchedulerListenerPrxHelper result = new SchedulerListenerPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
