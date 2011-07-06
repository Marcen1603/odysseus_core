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

public final class ImageMessagePrxHelper extends Ice.ObjectPrxHelperBase implements ImageMessagePrx
{
    public static ImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::ImageMessage"))
                {
                    ImageMessagePrxHelper __h = new ImageMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::ImageMessage", __ctx))
                {
                    ImageMessagePrxHelper __h = new ImageMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::ImageMessage"))
                {
                    ImageMessagePrxHelper __h = new ImageMessagePrxHelper();
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

    public static ImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::ImageMessage", __ctx))
                {
                    ImageMessagePrxHelper __h = new ImageMessagePrxHelper();
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

    public static ImageMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ImageMessagePrxHelper __h = new ImageMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ImageMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ImageMessagePrxHelper __h = new ImageMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ImageMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ImageMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ImageMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static ImageMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ImageMessagePrxHelper result = new ImageMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
