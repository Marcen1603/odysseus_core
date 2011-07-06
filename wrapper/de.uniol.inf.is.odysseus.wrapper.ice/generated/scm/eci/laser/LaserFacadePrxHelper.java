// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.laser;

public final class LaserFacadePrxHelper extends Ice.ObjectPrxHelperBase implements LaserFacadePrx
{
    public LaserscannerPrx
    createNewLaserscanner()
    {
        return createNewLaserscanner(null, false);
    }

    public LaserscannerPrx
    createNewLaserscanner(java.util.Map<String, String> __ctx)
    {
        return createNewLaserscanner(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private LaserscannerPrx
    createNewLaserscanner(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("createNewLaserscanner");
                __delBase = __getDelegate(false);
                _LaserFacadeDel __del = (_LaserFacadeDel)__delBase;
                return __del.createNewLaserscanner(__ctx);
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
                _LaserFacadeDel __del = (_LaserFacadeDel)__delBase;
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
                _LaserFacadeDel __del = (_LaserFacadeDel)__delBase;
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

    public static LaserFacadePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        LaserFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::LaserFacade"))
                {
                    LaserFacadePrxHelper __h = new LaserFacadePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static LaserFacadePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        LaserFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::LaserFacade", __ctx))
                {
                    LaserFacadePrxHelper __h = new LaserFacadePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static LaserFacadePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        LaserFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::LaserFacade"))
                {
                    LaserFacadePrxHelper __h = new LaserFacadePrxHelper();
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

    public static LaserFacadePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        LaserFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::LaserFacade", __ctx))
                {
                    LaserFacadePrxHelper __h = new LaserFacadePrxHelper();
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

    public static LaserFacadePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        LaserFacadePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserFacadePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                LaserFacadePrxHelper __h = new LaserFacadePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static LaserFacadePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        LaserFacadePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            LaserFacadePrxHelper __h = new LaserFacadePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _LaserFacadeDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _LaserFacadeDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, LaserFacadePrx v)
    {
        __os.writeProxy(v);
    }

    public static LaserFacadePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            LaserFacadePrxHelper result = new LaserFacadePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
