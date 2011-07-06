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

public final class DebugRangeValuePrxHelper extends Ice.ObjectPrxHelperBase implements DebugRangeValuePrx
{
    public static DebugRangeValuePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        DebugRangeValuePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DebugRangeValuePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::DebugRangeValue"))
                {
                    DebugRangeValuePrxHelper __h = new DebugRangeValuePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DebugRangeValuePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DebugRangeValuePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DebugRangeValuePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::DebugRangeValue", __ctx))
                {
                    DebugRangeValuePrxHelper __h = new DebugRangeValuePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DebugRangeValuePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DebugRangeValuePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::DebugRangeValue"))
                {
                    DebugRangeValuePrxHelper __h = new DebugRangeValuePrxHelper();
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

    public static DebugRangeValuePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DebugRangeValuePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::DebugRangeValue", __ctx))
                {
                    DebugRangeValuePrxHelper __h = new DebugRangeValuePrxHelper();
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

    public static DebugRangeValuePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        DebugRangeValuePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DebugRangeValuePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                DebugRangeValuePrxHelper __h = new DebugRangeValuePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DebugRangeValuePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DebugRangeValuePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DebugRangeValuePrxHelper __h = new DebugRangeValuePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _DebugRangeValueDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _DebugRangeValueDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, DebugRangeValuePrx v)
    {
        __os.writeProxy(v);
    }

    public static DebugRangeValuePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DebugRangeValuePrxHelper result = new DebugRangeValuePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
