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

public final class GrayImageMessagePrxHelper extends Ice.ObjectPrxHelperBase implements GrayImageMessagePrx
{
    public static GrayImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        GrayImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GrayImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::GrayImageMessage"))
                {
                    GrayImageMessagePrxHelper __h = new GrayImageMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static GrayImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        GrayImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GrayImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::GrayImageMessage", __ctx))
                {
                    GrayImageMessagePrxHelper __h = new GrayImageMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static GrayImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        GrayImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::GrayImageMessage"))
                {
                    GrayImageMessagePrxHelper __h = new GrayImageMessagePrxHelper();
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

    public static GrayImageMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        GrayImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::GrayImageMessage", __ctx))
                {
                    GrayImageMessagePrxHelper __h = new GrayImageMessagePrxHelper();
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

    public static GrayImageMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        GrayImageMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GrayImageMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                GrayImageMessagePrxHelper __h = new GrayImageMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static GrayImageMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        GrayImageMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            GrayImageMessagePrxHelper __h = new GrayImageMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _GrayImageMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _GrayImageMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, GrayImageMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static GrayImageMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            GrayImageMessagePrxHelper result = new GrayImageMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
