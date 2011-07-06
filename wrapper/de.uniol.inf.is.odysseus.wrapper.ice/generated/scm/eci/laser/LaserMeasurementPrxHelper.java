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

public final class LaserMeasurementPrxHelper extends Ice.ObjectPrxHelperBase implements LaserMeasurementPrx
{
    public static LaserMeasurementPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        LaserMeasurementPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserMeasurementPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::LaserMeasurement"))
                {
                    LaserMeasurementPrxHelper __h = new LaserMeasurementPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static LaserMeasurementPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        LaserMeasurementPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserMeasurementPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::LaserMeasurement", __ctx))
                {
                    LaserMeasurementPrxHelper __h = new LaserMeasurementPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static LaserMeasurementPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        LaserMeasurementPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::LaserMeasurement"))
                {
                    LaserMeasurementPrxHelper __h = new LaserMeasurementPrxHelper();
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

    public static LaserMeasurementPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        LaserMeasurementPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::LaserMeasurement", __ctx))
                {
                    LaserMeasurementPrxHelper __h = new LaserMeasurementPrxHelper();
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

    public static LaserMeasurementPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        LaserMeasurementPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserMeasurementPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                LaserMeasurementPrxHelper __h = new LaserMeasurementPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static LaserMeasurementPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        LaserMeasurementPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            LaserMeasurementPrxHelper __h = new LaserMeasurementPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _LaserMeasurementDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _LaserMeasurementDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, LaserMeasurementPrx v)
    {
        __os.writeProxy(v);
    }

    public static LaserMeasurementPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            LaserMeasurementPrxHelper result = new LaserMeasurementPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
