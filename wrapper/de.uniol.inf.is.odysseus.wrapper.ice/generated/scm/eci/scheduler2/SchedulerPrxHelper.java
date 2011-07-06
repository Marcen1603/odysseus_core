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

public final class SchedulerPrxHelper extends Ice.ObjectPrxHelperBase implements SchedulerPrx
{
    public void
    executeNextJob()
    {
        executeNextJob(null, false);
    }

    public void
    executeNextJob(java.util.Map<String, String> __ctx)
    {
        executeNextJob(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    executeNextJob(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                __del.executeNextJob(__ctx);
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

    public JobProcessorPrx
    getDefaultProcessor()
    {
        return getDefaultProcessor(null, false);
    }

    public JobProcessorPrx
    getDefaultProcessor(java.util.Map<String, String> __ctx)
    {
        return getDefaultProcessor(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private JobProcessorPrx
    getDefaultProcessor(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getDefaultProcessor");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.getDefaultProcessor(__ctx);
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
    getDescription()
    {
        return getDescription(null, false);
    }

    public String
    getDescription(java.util.Map<String, String> __ctx)
    {
        return getDescription(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getDescription(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getDescription");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.getDescription(__ctx);
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
    getName()
    {
        return getName(null, false);
    }

    public String
    getName(java.util.Map<String, String> __ctx)
    {
        return getName(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getName(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getName");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.getName(__ctx);
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

    public StrategiePrx
    getStrategie()
    {
        return getStrategie(null, false);
    }

    public StrategiePrx
    getStrategie(java.util.Map<String, String> __ctx)
    {
        return getStrategie(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private StrategiePrx
    getStrategie(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getStrategie");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.getStrategie(__ctx);
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

    public int
    getTimestamp()
    {
        return getTimestamp(null, false);
    }

    public int
    getTimestamp(java.util.Map<String, String> __ctx)
    {
        return getTimestamp(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private int
    getTimestamp(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getTimestamp");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.getTimestamp(__ctx);
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
    hasNextJob()
    {
        return hasNextJob(null, false);
    }

    public boolean
    hasNextJob(java.util.Map<String, String> __ctx)
    {
        return hasNextJob(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    hasNextJob(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("hasNextJob");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.hasNextJob(__ctx);
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
    hasNextStep()
    {
        return hasNextStep(null, false);
    }

    public boolean
    hasNextStep(java.util.Map<String, String> __ctx)
    {
        return hasNextStep(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    hasNextStep(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("hasNextStep");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.hasNextStep(__ctx);
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
    insert(JobDescription jobDesc)
    {
        return insert(jobDesc, null, false);
    }

    public boolean
    insert(JobDescription jobDesc, java.util.Map<String, String> __ctx)
    {
        return insert(jobDesc, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    insert(JobDescription jobDesc, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("insert");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.insert(jobDesc, __ctx);
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

    public int
    nextStep()
    {
        return nextStep(null, false);
    }

    public int
    nextStep(java.util.Map<String, String> __ctx)
    {
        return nextStep(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private int
    nextStep(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("nextStep");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.nextStep(__ctx);
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
    registerListener(SchedulerListenerPrx listener)
    {
        registerListener(listener, null, false);
    }

    public void
    registerListener(SchedulerListenerPrx listener, java.util.Map<String, String> __ctx)
    {
        registerListener(listener, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    registerListener(SchedulerListenerPrx listener, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                __del.registerListener(listener, __ctx);
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

    public boolean
    remove(JobPrx jobDesc)
    {
        return remove(jobDesc, null, false);
    }

    public boolean
    remove(JobPrx jobDesc, java.util.Map<String, String> __ctx)
    {
        return remove(jobDesc, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    remove(JobPrx jobDesc, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("remove");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.remove(jobDesc, __ctx);
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
    removeListener(SchedulerListenerPrx listener)
    {
        removeListener(listener, null, false);
    }

    public void
    removeListener(SchedulerListenerPrx listener, java.util.Map<String, String> __ctx)
    {
        removeListener(listener, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    removeListener(SchedulerListenerPrx listener, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                __del.removeListener(listener, __ctx);
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

    public boolean
    setStrategie(StrategiePrx newValue)
    {
        return setStrategie(newValue, null, false);
    }

    public boolean
    setStrategie(StrategiePrx newValue, java.util.Map<String, String> __ctx)
    {
        return setStrategie(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setStrategie(StrategiePrx newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setStrategie");
                __delBase = __getDelegate(false);
                _SchedulerDel __del = (_SchedulerDel)__delBase;
                return __del.setStrategie(newValue, __ctx);
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

    public static SchedulerPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        SchedulerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SchedulerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::Scheduler"))
                {
                    SchedulerPrxHelper __h = new SchedulerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SchedulerPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        SchedulerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SchedulerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::Scheduler", __ctx))
                {
                    SchedulerPrxHelper __h = new SchedulerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SchedulerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SchedulerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::Scheduler"))
                {
                    SchedulerPrxHelper __h = new SchedulerPrxHelper();
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

    public static SchedulerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        SchedulerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::Scheduler", __ctx))
                {
                    SchedulerPrxHelper __h = new SchedulerPrxHelper();
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

    public static SchedulerPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        SchedulerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SchedulerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                SchedulerPrxHelper __h = new SchedulerPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static SchedulerPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SchedulerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            SchedulerPrxHelper __h = new SchedulerPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _SchedulerDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _SchedulerDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, SchedulerPrx v)
    {
        __os.writeProxy(v);
    }

    public static SchedulerPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            SchedulerPrxHelper result = new SchedulerPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
