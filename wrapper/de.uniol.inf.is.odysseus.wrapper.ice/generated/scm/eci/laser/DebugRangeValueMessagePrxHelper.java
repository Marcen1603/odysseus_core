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

public final class DebugRangeValueMessagePrxHelper extends Ice.ObjectPrxHelperBase implements DebugRangeValueMessagePrx
{
    public static DebugRangeValueMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        DebugRangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DebugRangeValueMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::DebugRangeValueMessage"))
                {
                    DebugRangeValueMessagePrxHelper __h = new DebugRangeValueMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DebugRangeValueMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DebugRangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DebugRangeValueMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::DebugRangeValueMessage", __ctx))
                {
                    DebugRangeValueMessagePrxHelper __h = new DebugRangeValueMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DebugRangeValueMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DebugRangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::DebugRangeValueMessage"))
                {
                    DebugRangeValueMessagePrxHelper __h = new DebugRangeValueMessagePrxHelper();
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

    public static DebugRangeValueMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DebugRangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::DebugRangeValueMessage", __ctx))
                {
                    DebugRangeValueMessagePrxHelper __h = new DebugRangeValueMessagePrxHelper();
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

    public static DebugRangeValueMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        DebugRangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DebugRangeValueMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                DebugRangeValueMessagePrxHelper __h = new DebugRangeValueMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DebugRangeValueMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DebugRangeValueMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DebugRangeValueMessagePrxHelper __h = new DebugRangeValueMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _DebugRangeValueMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _DebugRangeValueMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, DebugRangeValueMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static DebugRangeValueMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DebugRangeValueMessagePrxHelper result = new DebugRangeValueMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
