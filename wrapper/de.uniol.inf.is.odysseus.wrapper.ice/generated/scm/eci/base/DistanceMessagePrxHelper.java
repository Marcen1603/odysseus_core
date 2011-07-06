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

public final class DistanceMessagePrxHelper extends Ice.ObjectPrxHelperBase implements DistanceMessagePrx
{
    public static DistanceMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        DistanceMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistanceMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::DistanceMessage"))
                {
                    DistanceMessagePrxHelper __h = new DistanceMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DistanceMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        DistanceMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistanceMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::DistanceMessage", __ctx))
                {
                    DistanceMessagePrxHelper __h = new DistanceMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static DistanceMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DistanceMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::DistanceMessage"))
                {
                    DistanceMessagePrxHelper __h = new DistanceMessagePrxHelper();
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

    public static DistanceMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        DistanceMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::DistanceMessage", __ctx))
                {
                    DistanceMessagePrxHelper __h = new DistanceMessagePrxHelper();
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

    public static DistanceMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        DistanceMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (DistanceMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                DistanceMessagePrxHelper __h = new DistanceMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static DistanceMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        DistanceMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            DistanceMessagePrxHelper __h = new DistanceMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _DistanceMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _DistanceMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, DistanceMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static DistanceMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            DistanceMessagePrxHelper result = new DistanceMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
