// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.vision;

public final class DistanceImagePrxHelper extends Ice.ObjectPrxHelperBase implements DistanceImagePrx
{
    public int
    channels()
    {
        return channels(null, false);
    }

    public int
    channels(java.util.Map<String, String> __ctx)
    {
        return channels(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private int
    channels(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("channels");
                __delBase = __getDelegate(false);
                _DistanceImageDel __del = (_DistanceImageDel)__delBase;
                return __del.channels(__ctx);
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
    depth()
    {
        return depth(null, false);
    }

    public int
    depth(java.util.Map<String, String> __ctx)
    {
        return depth(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private int
    depth(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("depth");
                __delBase = __getDelegate(false);
                _DistanceImageDel __del = (_DistanceImageDel)__delBase;
                return __del.depth(__ctx);
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

    public static DistanceImagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        DistanceImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistanceImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::DistanceImage"))
                {
                    DistanceImagePrxHelper __h = new DistanceImagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DistanceImagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DistanceImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistanceImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::DistanceImage", __ctx))
                {
                    DistanceImagePrxHelper __h = new DistanceImagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DistanceImagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DistanceImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::DistanceImage"))
                {
                    DistanceImagePrxHelper __h = new DistanceImagePrxHelper();
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

    public static DistanceImagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DistanceImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::DistanceImage", __ctx))
                {
                    DistanceImagePrxHelper __h = new DistanceImagePrxHelper();
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

    public static DistanceImagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        DistanceImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistanceImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                DistanceImagePrxHelper __h = new DistanceImagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DistanceImagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DistanceImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DistanceImagePrxHelper __h = new DistanceImagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _DistanceImageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _DistanceImageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, DistanceImagePrx v)
    {
        __os.writeProxy(v);
    }

    public static DistanceImagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DistanceImagePrxHelper result = new DistanceImagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
