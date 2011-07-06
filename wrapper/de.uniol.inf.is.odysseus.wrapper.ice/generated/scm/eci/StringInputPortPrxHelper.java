// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci;

public final class StringInputPortPrxHelper extends Ice.ObjectPrxHelperBase implements StringInputPortPrx
{
    public StringMessage
    getMessage()
    {
        return getMessage(null, false);
    }

    public StringMessage
    getMessage(java.util.Map<String, String> __ctx)
    {
        return getMessage(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private StringMessage
    getMessage(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getMessage");
                __delBase = __getDelegate(false);
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                return __del.getMessage(__ctx);
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
    getValue()
    {
        return getValue(null, false);
    }

    public String
    getValue(java.util.Map<String, String> __ctx)
    {
        return getValue(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getValue(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getValue");
                __delBase = __getDelegate(false);
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                return __del.getValue(__ctx);
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
    getCycle()
    {
        return getCycle(null, false);
    }

    public boolean
    getCycle(java.util.Map<String, String> __ctx)
    {
        return getCycle(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    getCycle(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getCycle");
                __delBase = __getDelegate(false);
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                return __del.getCycle(__ctx);
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
    getMayNull()
    {
        return getMayNull(null, false);
    }

    public boolean
    getMayNull(java.util.Map<String, String> __ctx)
    {
        return getMayNull(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    getMayNull(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getMayNull");
                __delBase = __getDelegate(false);
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                return __del.getMayNull(__ctx);
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

    public scm.eci.rt.InputPortPrx[]
    getNeighbors()
    {
        return getNeighbors(null, false);
    }

    public scm.eci.rt.InputPortPrx[]
    getNeighbors(java.util.Map<String, String> __ctx)
    {
        return getNeighbors(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.InputPortPrx[]
    getNeighbors(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getNeighbors");
                __delBase = __getDelegate(false);
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                return __del.getNeighbors(__ctx);
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
    getNoSync()
    {
        return getNoSync(null, false);
    }

    public boolean
    getNoSync(java.util.Map<String, String> __ctx)
    {
        return getNoSync(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    getNoSync(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getNoSync");
                __delBase = __getDelegate(false);
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                return __del.getNoSync(__ctx);
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

    public scm.eci.rt.OutputPortPrx
    getSource()
    {
        return getSource(null, false);
    }

    public scm.eci.rt.OutputPortPrx
    getSource(java.util.Map<String, String> __ctx)
    {
        return getSource(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.OutputPortPrx
    getSource(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getSource");
                __delBase = __getDelegate(false);
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                return __del.getSource(__ctx);
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
    hasSource()
    {
        return hasSource(null, false);
    }

    public boolean
    hasSource(java.util.Map<String, String> __ctx)
    {
        return hasSource(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    hasSource(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("hasSource");
                __delBase = __getDelegate(false);
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                return __del.hasSource(__ctx);
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

    public void
    receiveMessage(scm.eci.rt.IMessage msg)
    {
        receiveMessage(msg, null, false);
    }

    public void
    receiveMessage(scm.eci.rt.IMessage msg, java.util.Map<String, String> __ctx)
    {
        receiveMessage(msg, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    receiveMessage(scm.eci.rt.IMessage msg, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                __del.receiveMessage(msg, __ctx);
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

    public void
    resetSource()
    {
        resetSource(null, false);
    }

    public void
    resetSource(java.util.Map<String, String> __ctx)
    {
        resetSource(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    resetSource(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                __del.resetSource(__ctx);
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

    public boolean
    setNeighbors(scm.eci.rt.InputPortPrx[] newValue)
    {
        return setNeighbors(newValue, null, false);
    }

    public boolean
    setNeighbors(scm.eci.rt.InputPortPrx[] newValue, java.util.Map<String, String> __ctx)
    {
        return setNeighbors(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setNeighbors(scm.eci.rt.InputPortPrx[] newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setNeighbors");
                __delBase = __getDelegate(false);
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                return __del.setNeighbors(newValue, __ctx);
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

    public void
    setSource(scm.eci.rt.OutputPortPrx source)
    {
        setSource(source, null, false);
    }

    public void
    setSource(scm.eci.rt.OutputPortPrx source, java.util.Map<String, String> __ctx)
    {
        setSource(source, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    setSource(scm.eci.rt.OutputPortPrx source, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
                __del.setSource(source, __ctx);
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
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
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
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
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
                _StringInputPortDel __del = (_StringInputPortDel)__delBase;
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

    public static StringInputPortPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        StringInputPortPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (StringInputPortPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::StringInputPort"))
                {
                    StringInputPortPrxHelper __h = new StringInputPortPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static StringInputPortPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        StringInputPortPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (StringInputPortPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::StringInputPort", __ctx))
                {
                    StringInputPortPrxHelper __h = new StringInputPortPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static StringInputPortPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        StringInputPortPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::StringInputPort"))
                {
                    StringInputPortPrxHelper __h = new StringInputPortPrxHelper();
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

    public static StringInputPortPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        StringInputPortPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::StringInputPort", __ctx))
                {
                    StringInputPortPrxHelper __h = new StringInputPortPrxHelper();
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

    public static StringInputPortPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        StringInputPortPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (StringInputPortPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                StringInputPortPrxHelper __h = new StringInputPortPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static StringInputPortPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        StringInputPortPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            StringInputPortPrxHelper __h = new StringInputPortPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _StringInputPortDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _StringInputPortDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, StringInputPortPrx v)
    {
        __os.writeProxy(v);
    }

    public static StringInputPortPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            StringInputPortPrxHelper result = new StringInputPortPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
