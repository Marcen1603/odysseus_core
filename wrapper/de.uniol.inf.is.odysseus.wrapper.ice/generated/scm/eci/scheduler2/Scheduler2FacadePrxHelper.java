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

public final class Scheduler2FacadePrxHelper extends Ice.ObjectPrxHelperBase implements Scheduler2FacadePrx
{
    public SchedulerPrx
    createNewScheduler()
    {
        return createNewScheduler(null, false);
    }

    public SchedulerPrx
    createNewScheduler(java.util.Map<String, String> __ctx)
    {
        return createNewScheduler(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private SchedulerPrx
    createNewScheduler(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("createNewScheduler");
                __delBase = __getDelegate(false);
                _Scheduler2FacadeDel __del = (_Scheduler2FacadeDel)__delBase;
                return __del.createNewScheduler(__ctx);
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
    createNewStrategie()
    {
        return createNewStrategie(null, false);
    }

    public StrategiePrx
    createNewStrategie(java.util.Map<String, String> __ctx)
    {
        return createNewStrategie(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private StrategiePrx
    createNewStrategie(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("createNewStrategie");
                __delBase = __getDelegate(false);
                _Scheduler2FacadeDel __del = (_Scheduler2FacadeDel)__delBase;
                return __del.createNewStrategie(__ctx);
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

    public Ice.ObjectPrx
    getObjectByUID(String uid)
    {
        return getObjectByUID(uid, null, false);
    }

    public Ice.ObjectPrx
    getObjectByUID(String uid, java.util.Map<String, String> __ctx)
    {
        return getObjectByUID(uid, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private Ice.ObjectPrx
    getObjectByUID(String uid, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getObjectByUID");
                __delBase = __getDelegate(false);
                _Scheduler2FacadeDel __del = (_Scheduler2FacadeDel)__delBase;
                return __del.getObjectByUID(uid, __ctx);
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
    releaseObjectByUID(String uid)
    {
        releaseObjectByUID(uid, null, false);
    }

    public void
    releaseObjectByUID(String uid, java.util.Map<String, String> __ctx)
    {
        releaseObjectByUID(uid, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    releaseObjectByUID(String uid, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _Scheduler2FacadeDel __del = (_Scheduler2FacadeDel)__delBase;
                __del.releaseObjectByUID(uid, __ctx);
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

    public static Scheduler2FacadePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Scheduler2FacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Scheduler2FacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::Scheduler2Facade"))
                {
                    Scheduler2FacadePrxHelper __h = new Scheduler2FacadePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Scheduler2FacadePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Scheduler2FacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Scheduler2FacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::scheduler2::Scheduler2Facade", __ctx))
                {
                    Scheduler2FacadePrxHelper __h = new Scheduler2FacadePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Scheduler2FacadePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Scheduler2FacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::Scheduler2Facade"))
                {
                    Scheduler2FacadePrxHelper __h = new Scheduler2FacadePrxHelper();
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

    public static Scheduler2FacadePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Scheduler2FacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::scheduler2::Scheduler2Facade", __ctx))
                {
                    Scheduler2FacadePrxHelper __h = new Scheduler2FacadePrxHelper();
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

    public static Scheduler2FacadePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Scheduler2FacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Scheduler2FacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                Scheduler2FacadePrxHelper __h = new Scheduler2FacadePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Scheduler2FacadePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Scheduler2FacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Scheduler2FacadePrxHelper __h = new Scheduler2FacadePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Scheduler2FacadeDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Scheduler2FacadeDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Scheduler2FacadePrx v)
    {
        __os.writeProxy(v);
    }

    public static Scheduler2FacadePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Scheduler2FacadePrxHelper result = new Scheduler2FacadePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
