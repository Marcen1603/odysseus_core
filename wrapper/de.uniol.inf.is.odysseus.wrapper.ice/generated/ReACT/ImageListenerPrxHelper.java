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

public final class ImageListenerPrxHelper extends Ice.ObjectPrxHelperBase implements ImageListenerPrx
{
    public void
    receive(JPGImage img, long sendTime)
    {
        receive(img, sendTime, null, false);
    }

    public void
    receive(JPGImage img, long sendTime, java.util.Map<String, String> __ctx)
    {
        receive(img, sendTime, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    receive(JPGImage img, long sendTime, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _ImageListenerDel __del = (_ImageListenerDel)__delBase;
                __del.receive(img, sendTime, __ctx);
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

    public static ImageListenerPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ImageListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImageListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::ReACT::ImageListener"))
                {
                    ImageListenerPrxHelper __h = new ImageListenerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ImageListenerPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ImageListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImageListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::ReACT::ImageListener", __ctx))
                {
                    ImageListenerPrxHelper __h = new ImageListenerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ImageListenerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ImageListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::ReACT::ImageListener"))
                {
                    ImageListenerPrxHelper __h = new ImageListenerPrxHelper();
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

    public static ImageListenerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ImageListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::ReACT::ImageListener", __ctx))
                {
                    ImageListenerPrxHelper __h = new ImageListenerPrxHelper();
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

    public static ImageListenerPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ImageListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImageListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ImageListenerPrxHelper __h = new ImageListenerPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ImageListenerPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ImageListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ImageListenerPrxHelper __h = new ImageListenerPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ImageListenerDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ImageListenerDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ImageListenerPrx v)
    {
        __os.writeProxy(v);
    }

    public static ImageListenerPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ImageListenerPrxHelper result = new ImageListenerPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
