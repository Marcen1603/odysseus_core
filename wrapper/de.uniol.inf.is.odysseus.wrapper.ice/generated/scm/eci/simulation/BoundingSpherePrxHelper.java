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

public final class BoundingSpherePrxHelper extends Ice.ObjectPrxHelperBase implements BoundingSpherePrx
{
    public static BoundingSpherePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        BoundingSpherePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingSpherePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::BoundingSphere"))
                {
                    BoundingSpherePrxHelper __h = new BoundingSpherePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BoundingSpherePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        BoundingSpherePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingSpherePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::BoundingSphere", __ctx))
                {
                    BoundingSpherePrxHelper __h = new BoundingSpherePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BoundingSpherePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BoundingSpherePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::BoundingSphere"))
                {
                    BoundingSpherePrxHelper __h = new BoundingSpherePrxHelper();
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

    public static BoundingSpherePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        BoundingSpherePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::BoundingSphere", __ctx))
                {
                    BoundingSpherePrxHelper __h = new BoundingSpherePrxHelper();
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

    public static BoundingSpherePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        BoundingSpherePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingSpherePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                BoundingSpherePrxHelper __h = new BoundingSpherePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static BoundingSpherePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BoundingSpherePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            BoundingSpherePrxHelper __h = new BoundingSpherePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _BoundingSphereDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _BoundingSphereDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, BoundingSpherePrx v)
    {
        __os.writeProxy(v);
    }

    public static BoundingSpherePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            BoundingSpherePrxHelper result = new BoundingSpherePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
