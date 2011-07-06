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

public final class Polygon3PrxHelper extends Ice.ObjectPrxHelperBase implements Polygon3Prx
{
    public static Polygon3Prx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Polygon3Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Polygon3Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Polygon3"))
                {
                    Polygon3PrxHelper __h = new Polygon3PrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Polygon3Prx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Polygon3Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Polygon3Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Polygon3", __ctx))
                {
                    Polygon3PrxHelper __h = new Polygon3PrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Polygon3Prx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Polygon3Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Polygon3"))
                {
                    Polygon3PrxHelper __h = new Polygon3PrxHelper();
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

    public static Polygon3Prx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Polygon3Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Polygon3", __ctx))
                {
                    Polygon3PrxHelper __h = new Polygon3PrxHelper();
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

    public static Polygon3Prx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Polygon3Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Polygon3Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                Polygon3PrxHelper __h = new Polygon3PrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Polygon3Prx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Polygon3Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Polygon3PrxHelper __h = new Polygon3PrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Polygon3DelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Polygon3DelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Polygon3Prx v)
    {
        __os.writeProxy(v);
    }

    public static Polygon3Prx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Polygon3PrxHelper result = new Polygon3PrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
