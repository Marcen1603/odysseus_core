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

public final class Vector3PrxHelper extends Ice.ObjectPrxHelperBase implements Vector3Prx
{
    public static Vector3Prx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Vector3Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Vector3Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Vector3"))
                {
                    Vector3PrxHelper __h = new Vector3PrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Vector3Prx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Vector3Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Vector3Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Vector3", __ctx))
                {
                    Vector3PrxHelper __h = new Vector3PrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Vector3Prx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Vector3Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Vector3"))
                {
                    Vector3PrxHelper __h = new Vector3PrxHelper();
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

    public static Vector3Prx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Vector3Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Vector3", __ctx))
                {
                    Vector3PrxHelper __h = new Vector3PrxHelper();
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

    public static Vector3Prx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Vector3Prx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Vector3Prx)__obj;
            }
            catch(ClassCastException ex)
            {
                Vector3PrxHelper __h = new Vector3PrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Vector3Prx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Vector3Prx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Vector3PrxHelper __h = new Vector3PrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Vector3DelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Vector3DelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Vector3Prx v)
    {
        __os.writeProxy(v);
    }

    public static Vector3Prx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Vector3PrxHelper result = new Vector3PrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
