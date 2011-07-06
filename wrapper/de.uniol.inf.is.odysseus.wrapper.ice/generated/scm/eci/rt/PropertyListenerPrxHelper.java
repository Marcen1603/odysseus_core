// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.rt;

public final class PropertyListenerPrxHelper extends Ice.ObjectPrxHelperBase implements PropertyListenerPrx
{
    public void
    propertyChanged(PropertyPrx changedProperty)
    {
        propertyChanged(changedProperty, null, false);
    }

    public void
    propertyChanged(PropertyPrx changedProperty, java.util.Map<String, String> __ctx)
    {
        propertyChanged(changedProperty, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    propertyChanged(PropertyPrx changedProperty, java.util.Map<String, String> __ctx, boolean __explicitCtx)
    {
        if(__explicitCtx && __ctx == null)
        {
            __ctx = _emptyContext;
        }
        int __cnt = 0;
        while(true)
        {
            Ice._ObjectDel __delBase = null;
            try
            {
                __delBase = __getDelegate(false);
                _PropertyListenerDel __del = (_PropertyListenerDel)__delBase;
                __del.propertyChanged(changedProperty, __ctx);
                return;
            }
            catch(IceInternal.LocalExceptionWrapper __ex)
            {
                __handleExceptionWrapper(__delBase, __ex, null);
            }
            catch(Ice.LocalException __ex)
            {
                __cnt = __handleException(__delBase, __ex, null, __cnt);
            }
        }
    }

    public static PropertyListenerPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        PropertyListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PropertyListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::rt::PropertyListener"))
                {
                    PropertyListenerPrxHelper __h = new PropertyListenerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PropertyListenerPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        PropertyListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PropertyListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::rt::PropertyListener", __ctx))
                {
                    PropertyListenerPrxHelper __h = new PropertyListenerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PropertyListenerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PropertyListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::rt::PropertyListener"))
                {
                    PropertyListenerPrxHelper __h = new PropertyListenerPrxHelper();
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

    public static PropertyListenerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        PropertyListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::rt::PropertyListener", __ctx))
                {
                    PropertyListenerPrxHelper __h = new PropertyListenerPrxHelper();
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

    public static PropertyListenerPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        PropertyListenerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PropertyListenerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                PropertyListenerPrxHelper __h = new PropertyListenerPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static PropertyListenerPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PropertyListenerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            PropertyListenerPrxHelper __h = new PropertyListenerPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _PropertyListenerDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _PropertyListenerDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, PropertyListenerPrx v)
    {
        __os.writeProxy(v);
    }

    public static PropertyListenerPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            PropertyListenerPrxHelper result = new PropertyListenerPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
