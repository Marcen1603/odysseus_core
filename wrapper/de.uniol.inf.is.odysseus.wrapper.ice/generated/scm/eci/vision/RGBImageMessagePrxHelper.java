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

public final class RGBImageMessagePrxHelper extends Ice.ObjectPrxHelperBase implements RGBImageMessagePrx
{
    public static RGBImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        RGBImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RGBImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::RGBImageMessage"))
                {
                    RGBImageMessagePrxHelper __h = new RGBImageMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static RGBImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        RGBImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RGBImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::RGBImageMessage", __ctx))
                {
                    RGBImageMessagePrxHelper __h = new RGBImageMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static RGBImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        RGBImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::RGBImageMessage"))
                {
                    RGBImageMessagePrxHelper __h = new RGBImageMessagePrxHelper();
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

    public static RGBImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        RGBImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::RGBImageMessage", __ctx))
                {
                    RGBImageMessagePrxHelper __h = new RGBImageMessagePrxHelper();
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

    public static RGBImageMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        RGBImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RGBImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                RGBImageMessagePrxHelper __h = new RGBImageMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static RGBImageMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        RGBImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            RGBImageMessagePrxHelper __h = new RGBImageMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _RGBImageMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _RGBImageMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, RGBImageMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static RGBImageMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            RGBImageMessagePrxHelper result = new RGBImageMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
