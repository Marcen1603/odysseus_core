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

public final class ResourcePrxHelper extends Ice.ObjectPrxHelperBase implements ResourcePrx
{
    public static ResourcePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ResourcePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ResourcePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::Resource"))
                {
                    ResourcePrxHelper __h = new ResourcePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ResourcePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ResourcePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ResourcePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::Resource", __ctx))
                {
                    ResourcePrxHelper __h = new ResourcePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ResourcePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ResourcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::Resource"))
                {
                    ResourcePrxHelper __h = new ResourcePrxHelper();
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

    public static ResourcePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ResourcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::Resource", __ctx))
                {
                    ResourcePrxHelper __h = new ResourcePrxHelper();
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

    public static ResourcePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ResourcePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ResourcePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ResourcePrxHelper __h = new ResourcePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ResourcePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ResourcePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ResourcePrxHelper __h = new ResourcePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ResourceDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ResourceDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ResourcePrx v)
    {
        __os.writeProxy(v);
    }

    public static ResourcePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ResourcePrxHelper result = new ResourcePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
