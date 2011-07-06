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

public final class ProcessMonitorPrxHelper extends Ice.ObjectPrxHelperBase implements ProcessMonitorPrx
{
    public void
    setProgress(int progress, String msg)
    {
        setProgress(progress, msg, null, false);
    }

    public void
    setProgress(int progress, String msg, java.util.Map<String, String> __ctx)
    {
        setProgress(progress, msg, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    setProgress(int progress, String msg, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _ProcessMonitorDel __del = (_ProcessMonitorDel)__delBase;
                __del.setProgress(progress, msg, __ctx);
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
    setStatus(JobStatus status, String msg)
    {
        setStatus(status, msg, null, false);
    }

    public void
    setStatus(JobStatus status, String msg, java.util.Map<String, String> __ctx)
    {
        setStatus(status, msg, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    setStatus(JobStatus status, String msg, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _ProcessMonitorDel __del = (_ProcessMonitorDel)__delBase;
                __del.setStatus(status, msg, __ctx);
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

    public static ProcessMonitorPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ProcessMonitorPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ProcessMonitorPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::ProcessMonitor"))
                {
                    ProcessMonitorPrxHelper __h = new ProcessMonitorPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ProcessMonitorPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ProcessMonitorPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ProcessMonitorPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::ProcessMonitor", __ctx))
                {
                    ProcessMonitorPrxHelper __h = new ProcessMonitorPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ProcessMonitorPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ProcessMonitorPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::ProcessMonitor"))
                {
                    ProcessMonitorPrxHelper __h = new ProcessMonitorPrxHelper();
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

    public static ProcessMonitorPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ProcessMonitorPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::ProcessMonitor", __ctx))
                {
                    ProcessMonitorPrxHelper __h = new ProcessMonitorPrxHelper();
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

    public static ProcessMonitorPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ProcessMonitorPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ProcessMonitorPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ProcessMonitorPrxHelper __h = new ProcessMonitorPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ProcessMonitorPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ProcessMonitorPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ProcessMonitorPrxHelper __h = new ProcessMonitorPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ProcessMonitorDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ProcessMonitorDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ProcessMonitorPrx v)
    {
        __os.writeProxy(v);
    }

    public static ProcessMonitorPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ProcessMonitorPrxHelper result = new ProcessMonitorPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
