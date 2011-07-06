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

public final class GrayImagePrxHelper extends Ice.ObjectPrxHelperBase implements GrayImagePrx
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
                _GrayImageDel __del = (_GrayImageDel)__delBase;
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
                _GrayImageDel __del = (_GrayImageDel)__delBase;
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

    public static GrayImagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        GrayImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GrayImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::GrayImage"))
                {
                    GrayImagePrxHelper __h = new GrayImagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static GrayImagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        GrayImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GrayImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::GrayImage", __ctx))
                {
                    GrayImagePrxHelper __h = new GrayImagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static GrayImagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        GrayImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::GrayImage"))
                {
                    GrayImagePrxHelper __h = new GrayImagePrxHelper();
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

    public static GrayImagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        GrayImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::GrayImage", __ctx))
                {
                    GrayImagePrxHelper __h = new GrayImagePrxHelper();
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

    public static GrayImagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        GrayImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GrayImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                GrayImagePrxHelper __h = new GrayImagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static GrayImagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        GrayImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            GrayImagePrxHelper __h = new GrayImagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _GrayImageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _GrayImageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, GrayImagePrx v)
    {
        __os.writeProxy(v);
    }

    public static GrayImagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            GrayImagePrxHelper result = new GrayImagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
