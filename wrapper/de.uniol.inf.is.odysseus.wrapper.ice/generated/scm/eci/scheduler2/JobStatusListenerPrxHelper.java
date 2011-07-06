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

public final class JobStatusListenerPrxHelper extends Ice.ObjectPrxHelperBase implements JobStatusListenerPrx
{
    public void
    jobFinished(ExecutionContext ctx)
    {
        jobFinished(ctx, null, false);
    }

    public void
    jobFinished(ExecutionContext ctx, java.util.Map<String, String> __ctx)
    {
        jobFinished(ctx, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    jobFinished(ExecutionContext ctx, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _JobStatusListenerDel __del = (_JobStatusListenerDel)__delBase;
                __del.jobFinished(ctx, __ctx);
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

    public static JobStatusListenerPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        JobStatusListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobStatusListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::JobStatusListener"))
                {
                    JobStatusListenerPrxHelper __h = new JobStatusListenerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static JobStatusListenerPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        JobStatusListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobStatusListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::JobStatusListener", __ctx))
                {
                    JobStatusListenerPrxHelper __h = new JobStatusListenerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static JobStatusListenerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        JobStatusListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::JobStatusListener"))
                {
                    JobStatusListenerPrxHelper __h = new JobStatusListenerPrxHelper();
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

    public static JobStatusListenerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        JobStatusListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::JobStatusListener", __ctx))
                {
                    JobStatusListenerPrxHelper __h = new JobStatusListenerPrxHelper();
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

    public static JobStatusListenerPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        JobStatusListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (JobStatusListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                JobStatusListenerPrxHelper __h = new JobStatusListenerPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static JobStatusListenerPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        JobStatusListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            JobStatusListenerPrxHelper __h = new JobStatusListenerPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _JobStatusListenerDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _JobStatusListenerDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, JobStatusListenerPrx v)
    {
        __os.writeProxy(v);
    }

    public static JobStatusListenerPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            JobStatusListenerPrxHelper result = new JobStatusListenerPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
