// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package ReACT;

public final class ImageServerPrxHelper extends Ice.ObjectPrxHelperBase implements ImageServerPrx
{
    public void
    registerListener(ImageListenerPrx proxy, int width, int height, int quality)
    {
        registerListener(proxy, width, height, quality, null, false);
    }

    public void
    registerListener(ImageListenerPrx proxy, int width, int height, int quality, java.util.Map<String, String> __ctx)
    {
        registerListener(proxy, width, height, quality, __ctx, true);
    }

    private void
    registerListener(ImageListenerPrx proxy, int width, int height, int quality, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _ImageServerDel __del = (_ImageServerDel)__delBase;
                __del.registerListener(proxy, width, height, quality, __ctx);
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
    removeListener(ImageListenerPrx proxy)
    {
        removeListener(proxy, null, false);
    }

    public void
    removeListener(ImageListenerPrx proxy, java.util.Map<String, String> __ctx)
    {
        removeListener(proxy, __ctx, true);
    }

    private void
    removeListener(ImageListenerPrx proxy, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _ImageServerDel __del = (_ImageServerDel)__delBase;
                __del.removeListener(proxy, __ctx);
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

    public static ImageServerPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ImageServerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImageServerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::ReACT::ImageServer"))
                {
                    ImageServerPrxHelper __h = new ImageServerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ImageServerPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ImageServerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImageServerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::ReACT::ImageServer", __ctx))
                {
                    ImageServerPrxHelper __h = new ImageServerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ImageServerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ImageServerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::ReACT::ImageServer"))
                {
                    ImageServerPrxHelper __h = new ImageServerPrxHelper();
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

    public static ImageServerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ImageServerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::ReACT::ImageServer", __ctx))
                {
                    ImageServerPrxHelper __h = new ImageServerPrxHelper();
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

    public static ImageServerPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ImageServerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImageServerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ImageServerPrxHelper __h = new ImageServerPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ImageServerPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ImageServerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ImageServerPrxHelper __h = new ImageServerPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ImageServerDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ImageServerDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ImageServerPrx v)
    {
        __os.writeProxy(v);
    }

    public static ImageServerPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ImageServerPrxHelper result = new ImageServerPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
