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

public final class RGBImagePrxHelper extends Ice.ObjectPrxHelperBase implements RGBImagePrx
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
                _RGBImageDel __del = (_RGBImageDel)__delBase;
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
                _RGBImageDel __del = (_RGBImageDel)__delBase;
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

    public static RGBImagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        RGBImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RGBImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::RGBImage"))
                {
                    RGBImagePrxHelper __h = new RGBImagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static RGBImagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        RGBImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RGBImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::RGBImage", __ctx))
                {
                    RGBImagePrxHelper __h = new RGBImagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static RGBImagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        RGBImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::RGBImage"))
                {
                    RGBImagePrxHelper __h = new RGBImagePrxHelper();
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

    public static RGBImagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        RGBImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::RGBImage", __ctx))
                {
                    RGBImagePrxHelper __h = new RGBImagePrxHelper();
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

    public static RGBImagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        RGBImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RGBImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                RGBImagePrxHelper __h = new RGBImagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static RGBImagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        RGBImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            RGBImagePrxHelper __h = new RGBImagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _RGBImageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _RGBImageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, RGBImagePrx v)
    {
        __os.writeProxy(v);
    }

    public static RGBImagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            RGBImagePrxHelper result = new RGBImagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
