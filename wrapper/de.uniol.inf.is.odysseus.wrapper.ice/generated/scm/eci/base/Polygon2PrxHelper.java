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

public final class Polygon2PrxHelper extends Ice.ObjectPrxHelperBase implements Polygon2Prx
{
    public static Polygon2Prx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Polygon2Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Polygon2Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Polygon2"))
                {
                    Polygon2PrxHelper __h = new Polygon2PrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Polygon2Prx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Polygon2Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Polygon2Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Polygon2", __ctx))
                {
                    Polygon2PrxHelper __h = new Polygon2PrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Polygon2Prx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Polygon2Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Polygon2"))
                {
                    Polygon2PrxHelper __h = new Polygon2PrxHelper();
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

    public static Polygon2Prx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Polygon2Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Polygon2", __ctx))
                {
                    Polygon2PrxHelper __h = new Polygon2PrxHelper();
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

    public static Polygon2Prx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Polygon2Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Polygon2Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                Polygon2PrxHelper __h = new Polygon2PrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Polygon2Prx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Polygon2Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Polygon2PrxHelper __h = new Polygon2PrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Polygon2DelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Polygon2DelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Polygon2Prx v)
    {
        __os.writeProxy(v);
    }

    public static Polygon2Prx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Polygon2PrxHelper result = new Polygon2PrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
