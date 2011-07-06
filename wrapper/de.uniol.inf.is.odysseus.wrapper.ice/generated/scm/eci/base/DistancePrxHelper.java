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

public final class DistancePrxHelper extends Ice.ObjectPrxHelperBase implements DistancePrx
{
    public static DistancePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        DistancePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistancePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Distance"))
                {
                    DistancePrxHelper __h = new DistancePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DistancePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DistancePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistancePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Distance", __ctx))
                {
                    DistancePrxHelper __h = new DistancePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DistancePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DistancePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Distance"))
                {
                    DistancePrxHelper __h = new DistancePrxHelper();
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

    public static DistancePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DistancePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Distance", __ctx))
                {
                    DistancePrxHelper __h = new DistancePrxHelper();
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

    public static DistancePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        DistancePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistancePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                DistancePrxHelper __h = new DistancePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DistancePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DistancePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DistancePrxHelper __h = new DistancePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _DistanceDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _DistanceDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, DistancePrx v)
    {
        __os.writeProxy(v);
    }

    public static DistancePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DistancePrxHelper result = new DistancePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
