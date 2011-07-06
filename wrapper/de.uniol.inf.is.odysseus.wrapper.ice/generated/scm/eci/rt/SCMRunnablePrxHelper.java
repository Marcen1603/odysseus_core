// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.rt;

public final class SCMRunnablePrxHelper extends Ice.ObjectPrxHelperBase implements SCMRunnablePrx
{
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
                _SCMRunnableDel __del = (_SCMRunnableDel)__delBase;
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
                _SCMRunnableDel __del = (_SCMRunnableDel)__delBase;
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

    public String
    getUid()
    {
        return getUid(null, false);
    }

    public String
    getUid(java.util.Map<String, String> __ctx)
    {
        return getUid(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getUid(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getUid");
                __delBase = __getDelegate(false);
                _SCMRunnableDel __del = (_SCMRunnableDel)__delBase;
                return __del.getUid(__ctx);
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
    run()
    {
        run(null, false);
    }

    public void
    run(java.util.Map<String, String> __ctx)
    {
        run(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    run(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SCMRunnableDel __del = (_SCMRunnableDel)__delBase;
                __del.run(__ctx);
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

    public static SCMRunnablePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        SCMRunnablePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SCMRunnablePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::rt::SCMRunnable"))
                {
                    SCMRunnablePrxHelper __h = new SCMRunnablePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SCMRunnablePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        SCMRunnablePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SCMRunnablePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::rt::SCMRunnable", __ctx))
                {
                    SCMRunnablePrxHelper __h = new SCMRunnablePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SCMRunnablePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SCMRunnablePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::rt::SCMRunnable"))
                {
                    SCMRunnablePrxHelper __h = new SCMRunnablePrxHelper();
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

    public static SCMRunnablePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        SCMRunnablePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::rt::SCMRunnable", __ctx))
                {
                    SCMRunnablePrxHelper __h = new SCMRunnablePrxHelper();
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

    public static SCMRunnablePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        SCMRunnablePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SCMRunnablePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                SCMRunnablePrxHelper __h = new SCMRunnablePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static SCMRunnablePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SCMRunnablePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            SCMRunnablePrxHelper __h = new SCMRunnablePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _SCMRunnableDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _SCMRunnableDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, SCMRunnablePrx v)
    {
        __os.writeProxy(v);
    }

    public static SCMRunnablePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            SCMRunnablePrxHelper result = new SCMRunnablePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
