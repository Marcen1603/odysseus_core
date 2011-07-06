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

public final class ImagePrxHelper extends Ice.ObjectPrxHelperBase implements ImagePrx
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
                _ImageDel __del = (_ImageDel)__delBase;
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
                _ImageDel __del = (_ImageDel)__delBase;
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

    public static ImagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::Image"))
                {
                    ImagePrxHelper __h = new ImagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ImagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::Image", __ctx))
                {
                    ImagePrxHelper __h = new ImagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ImagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::Image"))
                {
                    ImagePrxHelper __h = new ImagePrxHelper();
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

    public static ImagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::Image", __ctx))
                {
                    ImagePrxHelper __h = new ImagePrxHelper();
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

    public static ImagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ImagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ImagePrxHelper __h = new ImagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ImagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ImagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ImagePrxHelper __h = new ImagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ImageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ImageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ImagePrx v)
    {
        __os.writeProxy(v);
    }

    public static ImagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ImagePrxHelper result = new ImagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
