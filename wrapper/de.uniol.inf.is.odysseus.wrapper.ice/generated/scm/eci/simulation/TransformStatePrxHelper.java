// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.simulation;

public final class TransformStatePrxHelper extends Ice.ObjectPrxHelperBase implements TransformStatePrx
{
    public scm.eci.base.Vector3
    getEuler()
    {
        return getEuler(null, false);
    }

    public scm.eci.base.Vector3
    getEuler(java.util.Map<String, String> __ctx)
    {
        return getEuler(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.base.Vector3
    getEuler(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getEuler");
                __delBase = __getDelegate(false);
                _TransformStateDel __del = (_TransformStateDel)__delBase;
                return __del.getEuler(__ctx);
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

    public scm.eci.base.Vector3
    getPosition()
    {
        return getPosition(null, false);
    }

    public scm.eci.base.Vector3
    getPosition(java.util.Map<String, String> __ctx)
    {
        return getPosition(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.base.Vector3
    getPosition(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getPosition");
                __delBase = __getDelegate(false);
                _TransformStateDel __del = (_TransformStateDel)__delBase;
                return __del.getPosition(__ctx);
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

    public scm.eci.base.Vector3
    getScale()
    {
        return getScale(null, false);
    }

    public scm.eci.base.Vector3
    getScale(java.util.Map<String, String> __ctx)
    {
        return getScale(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.base.Vector3
    getScale(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getScale");
                __delBase = __getDelegate(false);
                _TransformStateDel __del = (_TransformStateDel)__delBase;
                return __del.getScale(__ctx);
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

    public static TransformStatePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        TransformStatePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (TransformStatePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::TransformState"))
                {
                    TransformStatePrxHelper __h = new TransformStatePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static TransformStatePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        TransformStatePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (TransformStatePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::TransformState", __ctx))
                {
                    TransformStatePrxHelper __h = new TransformStatePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static TransformStatePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        TransformStatePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::TransformState"))
                {
                    TransformStatePrxHelper __h = new TransformStatePrxHelper();
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

    public static TransformStatePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        TransformStatePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::TransformState", __ctx))
                {
                    TransformStatePrxHelper __h = new TransformStatePrxHelper();
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

    public static TransformStatePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        TransformStatePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (TransformStatePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                TransformStatePrxHelper __h = new TransformStatePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static TransformStatePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        TransformStatePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            TransformStatePrxHelper __h = new TransformStatePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _TransformStateDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _TransformStateDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, TransformStatePrx v)
    {
        __os.writeProxy(v);
    }

    public static TransformStatePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            TransformStatePrxHelper result = new TransformStatePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
