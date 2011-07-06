// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.base;

public final class Polygon3MessagePrxHelper extends Ice.ObjectPrxHelperBase implements Polygon3MessagePrx
{
    public static Polygon3MessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Polygon3MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Polygon3MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Polygon3Message"))
                {
                    Polygon3MessagePrxHelper __h = new Polygon3MessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Polygon3MessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Polygon3MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Polygon3MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Polygon3Message", __ctx))
                {
                    Polygon3MessagePrxHelper __h = new Polygon3MessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Polygon3MessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Polygon3MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Polygon3Message"))
                {
                    Polygon3MessagePrxHelper __h = new Polygon3MessagePrxHelper();
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

    public static Polygon3MessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Polygon3MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Polygon3Message", __ctx))
                {
                    Polygon3MessagePrxHelper __h = new Polygon3MessagePrxHelper();
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

    public static Polygon3MessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Polygon3MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Polygon3MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                Polygon3MessagePrxHelper __h = new Polygon3MessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Polygon3MessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Polygon3MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Polygon3MessagePrxHelper __h = new Polygon3MessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Polygon3MessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Polygon3MessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Polygon3MessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static Polygon3MessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Polygon3MessagePrxHelper result = new Polygon3MessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
