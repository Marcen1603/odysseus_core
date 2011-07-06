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

public final class Vector2MessagePrxHelper extends Ice.ObjectPrxHelperBase implements Vector2MessagePrx
{
    public static Vector2MessagePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        Vector2MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Vector2MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Vector2Message"))
                {
                    Vector2MessagePrxHelper __h = new Vector2MessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Vector2MessagePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        Vector2MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Vector2MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::base::Vector2Message", __ctx))
                {
                    Vector2MessagePrxHelper __h = new Vector2MessagePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static Vector2MessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Vector2MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Vector2Message"))
                {
                    Vector2MessagePrxHelper __h = new Vector2MessagePrxHelper();
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

    public static Vector2MessagePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        Vector2MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::base::Vector2Message", __ctx))
                {
                    Vector2MessagePrxHelper __h = new Vector2MessagePrxHelper();
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

    public static Vector2MessagePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        Vector2MessagePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (Vector2MessagePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                Vector2MessagePrxHelper __h = new Vector2MessagePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static Vector2MessagePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        Vector2MessagePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            Vector2MessagePrxHelper __h = new Vector2MessagePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _Vector2MessageDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _Vector2MessageDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, Vector2MessagePrx v)
    {
        __os.writeProxy(v);
    }

    public static Vector2MessagePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            Vector2MessagePrxHelper result = new Vector2MessagePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
