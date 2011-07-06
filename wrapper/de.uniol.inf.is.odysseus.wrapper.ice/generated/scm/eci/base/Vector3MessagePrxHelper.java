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

public final class Vector3MessagePrxHelper extends Ice.ObjectPrxHelperBase implements Vector3MessagePrx
{
    public static Vector3MessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Vector3MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Vector3MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Vector3Message"))
                {
                    Vector3MessagePrxHelper __h = new Vector3MessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Vector3MessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Vector3MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Vector3MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Vector3Message", __ctx))
                {
                    Vector3MessagePrxHelper __h = new Vector3MessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Vector3MessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Vector3MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Vector3Message"))
                {
                    Vector3MessagePrxHelper __h = new Vector3MessagePrxHelper();
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

    public static Vector3MessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Vector3MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Vector3Message", __ctx))
                {
                    Vector3MessagePrxHelper __h = new Vector3MessagePrxHelper();
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

    public static Vector3MessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Vector3MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Vector3MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                Vector3MessagePrxHelper __h = new Vector3MessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Vector3MessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Vector3MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Vector3MessagePrxHelper __h = new Vector3MessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Vector3MessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Vector3MessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Vector3MessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static Vector3MessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Vector3MessagePrxHelper result = new Vector3MessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
