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

public final class JobProcessorPrxHelper extends Ice.ObjectPrxHelperBase implements JobProcessorPrx
{
    public boolean
    executeJob(ExecutionContext execCont, JobStatusListenerPrx listener)
    {
        return executeJob(execCont, listener, null, false);
    }

    public boolean
    executeJob(ExecutionContext execCont, JobStatusListenerPrx listener, java.util.Map<String, String> __ctx)
    {
        return executeJob(execCont, listener, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    executeJob(ExecutionContext execCont, JobStatusListenerPrx listener, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("executeJob");
                __delBase = __getDelegate(false);
                _JobProcessorDel __del = (_JobProcessorDel)__delBase;
                return __del.executeJob(execCont, listener, __ctx);
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

    public static JobProcessorPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        JobProcessorPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobProcessorPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::JobProcessor"))
                {
                    JobProcessorPrxHelper __h = new JobProcessorPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static JobProcessorPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        JobProcessorPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobProcessorPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::JobProcessor", __ctx))
                {
                    JobProcessorPrxHelper __h = new JobProcessorPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static JobProcessorPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        JobProcessorPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::JobProcessor"))
                {
                    JobProcessorPrxHelper __h = new JobProcessorPrxHelper();
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

    public static JobProcessorPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        JobProcessorPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::JobProcessor", __ctx))
                {
                    JobProcessorPrxHelper __h = new JobProcessorPrxHelper();
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

    public static JobProcessorPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        JobProcessorPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobProcessorPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                JobProcessorPrxHelper __h = new JobProcessorPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static JobProcessorPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        JobProcessorPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            JobProcessorPrxHelper __h = new JobProcessorPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _JobProcessorDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _JobProcessorDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, JobProcessorPrx v)
    {
        __os.writeProxy(v);
    }

    public static JobProcessorPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            JobProcessorPrxHelper result = new JobProcessorPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
