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

public final class PropertyPrxHelper extends Ice.ObjectPrxHelperBase implements PropertyPrx
{
    public PropertyListenerPrx[]
    getListener()
    {
        return getListener(null, false);
    }

    public PropertyListenerPrx[]
    getListener(java.util.Map<String, String> __ctx)
    {
        return getListener(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private PropertyListenerPrx[]
    getListener(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getListener");
                __delBase = __getDelegate(false);
                _PropertyDel __del = (_PropertyDel)__delBase;
                return __del.getListener(__ctx);
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

    public boolean
    registerListener(PropertyListenerPrx listener)
    {
        return registerListener(listener, null, false);
    }

    public boolean
    registerListener(PropertyListenerPrx listener, java.util.Map<String, String> __ctx)
    {
        return registerListener(listener, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    registerListener(PropertyListenerPrx listener, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("registerListener");
                __delBase = __getDelegate(false);
                _PropertyDel __del = (_PropertyDel)__delBase;
                return __del.registerListener(listener, __ctx);
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

    public boolean
    removeListener(PropertyListenerPrx listener)
    {
        return removeListener(listener, null, false);
    }

    public boolean
    removeListener(PropertyListenerPrx listener, java.util.Map<String, String> __ctx)
    {
        return removeListener(listener, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    removeListener(PropertyListenerPrx listener, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("removeListener");
                __delBase = __getDelegate(false);
                _PropertyDel __del = (_PropertyDel)__delBase;
                return __del.removeListener(listener, __ctx);
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

    public boolean
    setListener(PropertyListenerPrx[] newValue)
    {
        return setListener(newValue, null, false);
    }

    public boolean
    setListener(PropertyListenerPrx[] newValue, java.util.Map<String, String> __ctx)
    {
        return setListener(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setListener(PropertyListenerPrx[] newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setListener");
                __delBase = __getDelegate(false);
                _PropertyDel __del = (_PropertyDel)__delBase;
                return __del.setListener(newValue, __ctx);
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

    public String
    getDescription()
    {
        return getDescription(null, false);
    }

    public String
    getDescription(java.util.Map<String, String> __ctx)
    {
        return getDescription(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getDescription(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getDescription");
                __delBase = __getDelegate(false);
                _PropertyDel __del = (_PropertyDel)__delBase;
                return __del.getDescription(__ctx);
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

    public String
    getName()
    {
        return getName(null, false);
    }

    public String
    getName(java.util.Map<String, String> __ctx)
    {
        return getName(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getName(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getName");
                __delBase = __getDelegate(false);
                _PropertyDel __del = (_PropertyDel)__delBase;
                return __del.getName(__ctx);
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

    public String
    getUid()
    {
        return getUid(null, false);
    }

    public String
    getUid(java.util.Map<String, String> __ctx)
    {
        return getUid(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getUid(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getUid");
                __delBase = __getDelegate(false);
                _PropertyDel __del = (_PropertyDel)__delBase;
                return __del.getUid(__ctx);
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

    public static PropertyPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        PropertyPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PropertyPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::rt::Property"))
                {
                    PropertyPrxHelper __h = new PropertyPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PropertyPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        PropertyPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PropertyPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::rt::Property", __ctx))
                {
                    PropertyPrxHelper __h = new PropertyPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static PropertyPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PropertyPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::rt::Property"))
                {
                    PropertyPrxHelper __h = new PropertyPrxHelper();
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

    public static PropertyPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        PropertyPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::rt::Property", __ctx))
                {
                    PropertyPrxHelper __h = new PropertyPrxHelper();
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

    public static PropertyPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        PropertyPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (PropertyPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                PropertyPrxHelper __h = new PropertyPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static PropertyPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        PropertyPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            PropertyPrxHelper __h = new PropertyPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _PropertyDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _PropertyDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, PropertyPrx v)
    {
        __os.writeProxy(v);
    }

    public static PropertyPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            PropertyPrxHelper result = new PropertyPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
