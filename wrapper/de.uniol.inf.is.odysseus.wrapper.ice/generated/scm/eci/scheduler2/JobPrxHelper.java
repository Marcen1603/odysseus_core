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

public final class JobPrxHelper extends Ice.ObjectPrxHelperBase implements JobPrx
{
    public boolean
    canExecute()
    {
        return canExecute(null, false);
    }

    public boolean
    canExecute(java.util.Map<String, String> __ctx)
    {
        return canExecute(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    canExecute(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("canExecute");
                __delBase = __getDelegate(false);
                _JobDel __del = (_JobDel)__delBase;
                return __del.canExecute(__ctx);
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

    public boolean
    execute(ExecutionContext ctx)
    {
        return execute(ctx, null, false);
    }

    public boolean
    execute(ExecutionContext ctx, java.util.Map<String, String> __ctx)
    {
        return execute(ctx, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    execute(ExecutionContext ctx, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("execute");
                __delBase = __getDelegate(false);
                _JobDel __del = (_JobDel)__delBase;
                return __del.execute(ctx, __ctx);
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

    public JobProcessorPrx
    getActiveProcessor()
    {
        return getActiveProcessor(null, false);
    }

    public JobProcessorPrx
    getActiveProcessor(java.util.Map<String, String> __ctx)
    {
        return getActiveProcessor(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private JobProcessorPrx
    getActiveProcessor(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getActiveProcessor");
                __delBase = __getDelegate(false);
                _JobDel __del = (_JobDel)__delBase;
                return __del.getActiveProcessor(__ctx);
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

    public String
    getJobDescription()
    {
        return getJobDescription(null, false);
    }

    public String
    getJobDescription(java.util.Map<String, String> __ctx)
    {
        return getJobDescription(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getJobDescription(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getJobDescription");
                __delBase = __getDelegate(false);
                _JobDel __del = (_JobDel)__delBase;
                return __del.getJobDescription(__ctx);
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

    public String
    getJobName()
    {
        return getJobName(null, false);
    }

    public String
    getJobName(java.util.Map<String, String> __ctx)
    {
        return getJobName(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getJobName(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getJobName");
                __delBase = __getDelegate(false);
                _JobDel __del = (_JobDel)__delBase;
                return __del.getJobName(__ctx);
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

    public boolean
    setActiveProcessor(JobProcessorPrx newValue)
    {
        return setActiveProcessor(newValue, null, false);
    }

    public boolean
    setActiveProcessor(JobProcessorPrx newValue, java.util.Map<String, String> __ctx)
    {
        return setActiveProcessor(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setActiveProcessor(JobProcessorPrx newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setActiveProcessor");
                __delBase = __getDelegate(false);
                _JobDel __del = (_JobDel)__delBase;
                return __del.setActiveProcessor(newValue, __ctx);
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

    public static JobPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        JobPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::Job"))
                {
                    JobPrxHelper __h = new JobPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static JobPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        JobPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::Job", __ctx))
                {
                    JobPrxHelper __h = new JobPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static JobPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        JobPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::Job"))
                {
                    JobPrxHelper __h = new JobPrxHelper();
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

    public static JobPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        JobPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::Job", __ctx))
                {
                    JobPrxHelper __h = new JobPrxHelper();
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

    public static JobPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        JobPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                JobPrxHelper __h = new JobPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static JobPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        JobPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            JobPrxHelper __h = new JobPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _JobDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _JobDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, JobPrx v)
    {
        __os.writeProxy(v);
    }

    public static JobPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            JobPrxHelper result = new JobPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
