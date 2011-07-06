// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.simulation;

public final class BoundingVolumePrxHelper extends Ice.ObjectPrxHelperBase implements BoundingVolumePrx
{
    public static BoundingVolumePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        BoundingVolumePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingVolumePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::BoundingVolume"))
                {
                    BoundingVolumePrxHelper __h = new BoundingVolumePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BoundingVolumePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        BoundingVolumePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingVolumePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::BoundingVolume", __ctx))
                {
                    BoundingVolumePrxHelper __h = new BoundingVolumePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BoundingVolumePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BoundingVolumePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::BoundingVolume"))
                {
                    BoundingVolumePrxHelper __h = new BoundingVolumePrxHelper();
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

    public static BoundingVolumePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        BoundingVolumePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::BoundingVolume", __ctx))
                {
                    BoundingVolumePrxHelper __h = new BoundingVolumePrxHelper();
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

    public static BoundingVolumePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        BoundingVolumePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingVolumePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                BoundingVolumePrxHelper __h = new BoundingVolumePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static BoundingVolumePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BoundingVolumePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            BoundingVolumePrxHelper __h = new BoundingVolumePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _BoundingVolumeDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _BoundingVolumeDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, BoundingVolumePrx v)
    {
        __os.writeProxy(v);
    }

    public static BoundingVolumePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            BoundingVolumePrxHelper result = new BoundingVolumePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
