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

public final class BoundingSphereMessagePrxHelper extends Ice.ObjectPrxHelperBase implements BoundingSphereMessagePrx
{
    public static BoundingSphereMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        BoundingSphereMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingSphereMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::BoundingSphereMessage"))
                {
                    BoundingSphereMessagePrxHelper __h = new BoundingSphereMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BoundingSphereMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        BoundingSphereMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingSphereMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::BoundingSphereMessage", __ctx))
                {
                    BoundingSphereMessagePrxHelper __h = new BoundingSphereMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static BoundingSphereMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BoundingSphereMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::BoundingSphereMessage"))
                {
                    BoundingSphereMessagePrxHelper __h = new BoundingSphereMessagePrxHelper();
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

    public static BoundingSphereMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        BoundingSphereMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::BoundingSphereMessage", __ctx))
                {
                    BoundingSphereMessagePrxHelper __h = new BoundingSphereMessagePrxHelper();
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

    public static BoundingSphereMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        BoundingSphereMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (BoundingSphereMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                BoundingSphereMessagePrxHelper __h = new BoundingSphereMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static BoundingSphereMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        BoundingSphereMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            BoundingSphereMessagePrxHelper __h = new BoundingSphereMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _BoundingSphereMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _BoundingSphereMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, BoundingSphereMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static BoundingSphereMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            BoundingSphereMessagePrxHelper result = new BoundingSphereMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
