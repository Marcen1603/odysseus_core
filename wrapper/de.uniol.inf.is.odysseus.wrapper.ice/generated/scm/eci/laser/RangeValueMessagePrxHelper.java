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

public final class RangeValueMessagePrxHelper extends Ice.ObjectPrxHelperBase implements RangeValueMessagePrx
{
    public static RangeValueMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        RangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RangeValueMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::RangeValueMessage"))
                {
                    RangeValueMessagePrxHelper __h = new RangeValueMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static RangeValueMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        RangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RangeValueMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::RangeValueMessage", __ctx))
                {
                    RangeValueMessagePrxHelper __h = new RangeValueMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static RangeValueMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        RangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::RangeValueMessage"))
                {
                    RangeValueMessagePrxHelper __h = new RangeValueMessagePrxHelper();
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

    public static RangeValueMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        RangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::RangeValueMessage", __ctx))
                {
                    RangeValueMessagePrxHelper __h = new RangeValueMessagePrxHelper();
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

    public static RangeValueMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        RangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (RangeValueMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                RangeValueMessagePrxHelper __h = new RangeValueMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static RangeValueMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        RangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            RangeValueMessagePrxHelper __h = new RangeValueMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _RangeValueMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _RangeValueMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, RangeValueMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static RangeValueMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            RangeValueMessagePrxHelper result = new RangeValueMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
