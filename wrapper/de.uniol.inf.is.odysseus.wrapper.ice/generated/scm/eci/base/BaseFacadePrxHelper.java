// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.base;

public final class BaseFacadePrxHelper extends Ice.ObjectPrxHelperBase implements BaseFacadePrx
{
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
                _BaseFacadeDel __del = (_BaseFacadeDel)__delBase;
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
                _BaseFacadeDel __del = (_BaseFacadeDel)__delBase;
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

    public static BaseFacadePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        BaseFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BaseFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::BaseFacade"))
                {
                    BaseFacadePrxHelper __h = new BaseFacadePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BaseFacadePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        BaseFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BaseFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::BaseFacade", __ctx))
                {
                    BaseFacadePrxHelper __h = new BaseFacadePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BaseFacadePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BaseFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::BaseFacade"))
                {
                    BaseFacadePrxHelper __h = new BaseFacadePrxHelper();
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

    public static BaseFacadePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        BaseFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::BaseFacade", __ctx))
                {
                    BaseFacadePrxHelper __h = new BaseFacadePrxHelper();
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

    public static BaseFacadePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        BaseFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BaseFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                BaseFacadePrxHelper __h = new BaseFacadePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static BaseFacadePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BaseFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            BaseFacadePrxHelper __h = new BaseFacadePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _BaseFacadeDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _BaseFacadeDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, BaseFacadePrx v)
    {
        __os.writeProxy(v);
    }

    public static BaseFacadePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            BaseFacadePrxHelper result = new BaseFacadePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
