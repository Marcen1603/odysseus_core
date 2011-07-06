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

public final class ResourceMessagePrxHelper extends Ice.ObjectPrxHelperBase implements ResourceMessagePrx
{
    public static ResourceMessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ResourceMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ResourceMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::ResourceMessage"))
                {
                    ResourceMessagePrxHelper __h = new ResourceMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ResourceMessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ResourceMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ResourceMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::ResourceMessage", __ctx))
                {
                    ResourceMessagePrxHelper __h = new ResourceMessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ResourceMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ResourceMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::ResourceMessage"))
                {
                    ResourceMessagePrxHelper __h = new ResourceMessagePrxHelper();
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

    public static ResourceMessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ResourceMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::ResourceMessage", __ctx))
                {
                    ResourceMessagePrxHelper __h = new ResourceMessagePrxHelper();
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

    public static ResourceMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ResourceMessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ResourceMessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ResourceMessagePrxHelper __h = new ResourceMessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ResourceMessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ResourceMessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ResourceMessagePrxHelper __h = new ResourceMessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _ResourceMessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _ResourceMessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ResourceMessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static ResourceMessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ResourceMessagePrxHelper result = new ResourceMessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
