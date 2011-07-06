// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.laser;

public final class RangeValuePrxHelper extends Ice.ObjectPrxHelperBase implements RangeValuePrx
{
    public static RangeValuePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        RangeValuePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RangeValuePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::RangeValue"))
                {
                    RangeValuePrxHelper __h = new RangeValuePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static RangeValuePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        RangeValuePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RangeValuePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::RangeValue", __ctx))
                {
                    RangeValuePrxHelper __h = new RangeValuePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static RangeValuePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        RangeValuePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::RangeValue"))
                {
                    RangeValuePrxHelper __h = new RangeValuePrxHelper();
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

    public static RangeValuePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        RangeValuePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::RangeValue", __ctx))
                {
                    RangeValuePrxHelper __h = new RangeValuePrxHelper();
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

    public static RangeValuePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        RangeValuePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RangeValuePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                RangeValuePrxHelper __h = new RangeValuePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static RangeValuePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        RangeValuePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            RangeValuePrxHelper __h = new RangeValuePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _RangeValueDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _RangeValueDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, RangeValuePrx v)
    {
        __os.writeProxy(v);
    }

    public static RangeValuePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            RangeValuePrxHelper result = new RangeValuePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
