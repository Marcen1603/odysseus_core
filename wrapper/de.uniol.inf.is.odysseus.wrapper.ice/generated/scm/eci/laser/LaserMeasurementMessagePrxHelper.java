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

public final class LaserMeasurementMessagePrxHelper extends Ice.ObjectPrxHelperBase implements LaserMeasurementMessagePrx
{
    public static LaserMeasurementMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        LaserMeasurementMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserMeasurementMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::LaserMeasurementMessage"))
                {
                    LaserMeasurementMessagePrxHelper __h = new LaserMeasurementMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static LaserMeasurementMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        LaserMeasurementMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserMeasurementMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::LaserMeasurementMessage", __ctx))
                {
                    LaserMeasurementMessagePrxHelper __h = new LaserMeasurementMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static LaserMeasurementMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        LaserMeasurementMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::LaserMeasurementMessage"))
                {
                    LaserMeasurementMessagePrxHelper __h = new LaserMeasurementMessagePrxHelper();
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

    public static LaserMeasurementMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        LaserMeasurementMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::LaserMeasurementMessage", __ctx))
                {
                    LaserMeasurementMessagePrxHelper __h = new LaserMeasurementMessagePrxHelper();
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

    public static LaserMeasurementMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        LaserMeasurementMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserMeasurementMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                LaserMeasurementMessagePrxHelper __h = new LaserMeasurementMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static LaserMeasurementMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        LaserMeasurementMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            LaserMeasurementMessagePrxHelper __h = new LaserMeasurementMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _LaserMeasurementMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _LaserMeasurementMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, LaserMeasurementMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static LaserMeasurementMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            LaserMeasurementMessagePrxHelper result = new LaserMeasurementMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
