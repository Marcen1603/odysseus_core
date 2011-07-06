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

public final class BoundingVolumeMessagePrxHelper extends Ice.ObjectPrxHelperBase implements BoundingVolumeMessagePrx
{
    public static BoundingVolumeMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        BoundingVolumeMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingVolumeMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::BoundingVolumeMessage"))
                {
                    BoundingVolumeMessagePrxHelper __h = new BoundingVolumeMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BoundingVolumeMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        BoundingVolumeMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingVolumeMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::BoundingVolumeMessage", __ctx))
                {
                    BoundingVolumeMessagePrxHelper __h = new BoundingVolumeMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BoundingVolumeMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BoundingVolumeMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::BoundingVolumeMessage"))
                {
                    BoundingVolumeMessagePrxHelper __h = new BoundingVolumeMessagePrxHelper();
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

    public static BoundingVolumeMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        BoundingVolumeMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::BoundingVolumeMessage", __ctx))
                {
                    BoundingVolumeMessagePrxHelper __h = new BoundingVolumeMessagePrxHelper();
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

    public static BoundingVolumeMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        BoundingVolumeMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingVolumeMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                BoundingVolumeMessagePrxHelper __h = new BoundingVolumeMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static BoundingVolumeMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BoundingVolumeMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            BoundingVolumeMessagePrxHelper __h = new BoundingVolumeMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _BoundingVolumeMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _BoundingVolumeMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, BoundingVolumeMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static BoundingVolumeMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            BoundingVolumeMessagePrxHelper result = new BoundingVolumeMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
