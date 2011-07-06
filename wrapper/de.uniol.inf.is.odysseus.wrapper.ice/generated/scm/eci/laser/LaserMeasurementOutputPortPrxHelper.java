// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.laser;

public final class LaserMeasurementOutputPortPrxHelper extends Ice.ObjectPrxHelperBase implements LaserMeasurementOutputPortPrx
{
    public void
    send(LaserMeasurement msg)
    {
        send(msg, null, false);
    }

    public void
    send(LaserMeasurement msg, java.util.Map<String, String> __ctx)
    {
        send(msg, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    send(LaserMeasurement msg, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
                __del.send(msg, __ctx);
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
    connect(scm.eci.rt.InputPortPrx target)
    {
        return connect(target, null, false);
    }

    public boolean
    connect(scm.eci.rt.InputPortPrx target, java.util.Map<String, String> __ctx)
    {
        return connect(target, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    connect(scm.eci.rt.InputPortPrx target, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("connect");
                __delBase = __getDelegate(false);
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
                return __del.connect(target, __ctx);
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
    disconnect(scm.eci.rt.InputPortPrx target)
    {
        return disconnect(target, null, false);
    }

    public boolean
    disconnect(scm.eci.rt.InputPortPrx target, java.util.Map<String, String> __ctx)
    {
        return disconnect(target, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    disconnect(scm.eci.rt.InputPortPrx target, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("disconnect");
                __delBase = __getDelegate(false);
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
                return __del.disconnect(target, __ctx);
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
    getAllowObserver()
    {
        return getAllowObserver(null, false);
    }

    public boolean
    getAllowObserver(java.util.Map<String, String> __ctx)
    {
        return getAllowObserver(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    getAllowObserver(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getAllowObserver");
                __delBase = __getDelegate(false);
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
                return __del.getAllowObserver(__ctx);
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

    public scm.eci.rt.PortListenerPrx[]
    getListener()
    {
        return getListener(null, false);
    }

    public scm.eci.rt.PortListenerPrx[]
    getListener(java.util.Map<String, String> __ctx)
    {
        return getListener(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.PortListenerPrx[]
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
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
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
    getSync()
    {
        return getSync(null, false);
    }

    public boolean
    getSync(java.util.Map<String, String> __ctx)
    {
        return getSync(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    getSync(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getSync");
                __delBase = __getDelegate(false);
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
                return __del.getSync(__ctx);
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
    getTargets()
    {
        return getTargets(null, false);
    }

    public scm.eci.rt.InputPortPrx[]
    getTargets(java.util.Map<String, String> __ctx)
    {
        return getTargets(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.InputPortPrx[]
    getTargets(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getTargets");
                __delBase = __getDelegate(false);
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
                return __del.getTargets(__ctx);
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
    registerListener(scm.eci.rt.PortListenerPrx listener)
    {
        return registerListener(listener, null, false);
    }

    public boolean
    registerListener(scm.eci.rt.PortListenerPrx listener, java.util.Map<String, String> __ctx)
    {
        return registerListener(listener, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    registerListener(scm.eci.rt.PortListenerPrx listener, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
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
    removeListener(scm.eci.rt.PortListenerPrx listener)
    {
        return removeListener(listener, null, false);
    }

    public boolean
    removeListener(scm.eci.rt.PortListenerPrx listener, java.util.Map<String, String> __ctx)
    {
        return removeListener(listener, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    removeListener(scm.eci.rt.PortListenerPrx listener, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
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

    public void
    sendMessage(scm.eci.rt.IMessage msg)
    {
        sendMessage(msg, null, false);
    }

    public void
    sendMessage(scm.eci.rt.IMessage msg, java.util.Map<String, String> __ctx)
    {
        sendMessage(msg, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    sendMessage(scm.eci.rt.IMessage msg, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
                __del.sendMessage(msg, __ctx);
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
    setAllowObserver(boolean newValue)
    {
        return setAllowObserver(newValue, null, false);
    }

    public boolean
    setAllowObserver(boolean newValue, java.util.Map<String, String> __ctx)
    {
        return setAllowObserver(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setAllowObserver(boolean newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setAllowObserver");
                __delBase = __getDelegate(false);
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
                return __del.setAllowObserver(newValue, __ctx);
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
    setListener(scm.eci.rt.PortListenerPrx[] newValue)
    {
        return setListener(newValue, null, false);
    }

    public boolean
    setListener(scm.eci.rt.PortListenerPrx[] newValue, java.util.Map<String, String> __ctx)
    {
        return setListener(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setListener(scm.eci.rt.PortListenerPrx[] newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
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

    public boolean
    setSync(boolean newValue)
    {
        return setSync(newValue, null, false);
    }

    public boolean
    setSync(boolean newValue, java.util.Map<String, String> __ctx)
    {
        return setSync(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setSync(boolean newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setSync");
                __delBase = __getDelegate(false);
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
                return __del.setSync(newValue, __ctx);
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
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
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
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
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
                _LaserMeasurementOutputPortDel __del = (_LaserMeasurementOutputPortDel)__delBase;
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

    public static LaserMeasurementOutputPortPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        LaserMeasurementOutputPortPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserMeasurementOutputPortPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::LaserMeasurementOutputPort"))
                {
                    LaserMeasurementOutputPortPrxHelper __h = new LaserMeasurementOutputPortPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static LaserMeasurementOutputPortPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        LaserMeasurementOutputPortPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserMeasurementOutputPortPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::laser::LaserMeasurementOutputPort", __ctx))
                {
                    LaserMeasurementOutputPortPrxHelper __h = new LaserMeasurementOutputPortPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static LaserMeasurementOutputPortPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        LaserMeasurementOutputPortPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::LaserMeasurementOutputPort"))
                {
                    LaserMeasurementOutputPortPrxHelper __h = new LaserMeasurementOutputPortPrxHelper();
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

    public static LaserMeasurementOutputPortPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        LaserMeasurementOutputPortPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::laser::LaserMeasurementOutputPort", __ctx))
                {
                    LaserMeasurementOutputPortPrxHelper __h = new LaserMeasurementOutputPortPrxHelper();
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

    public static LaserMeasurementOutputPortPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        LaserMeasurementOutputPortPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (LaserMeasurementOutputPortPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                LaserMeasurementOutputPortPrxHelper __h = new LaserMeasurementOutputPortPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static LaserMeasurementOutputPortPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        LaserMeasurementOutputPortPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            LaserMeasurementOutputPortPrxHelper __h = new LaserMeasurementOutputPortPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _LaserMeasurementOutputPortDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _LaserMeasurementOutputPortDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, LaserMeasurementOutputPortPrx v)
    {
        __os.writeProxy(v);
    }

    public static LaserMeasurementOutputPortPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            LaserMeasurementOutputPortPrxHelper result = new LaserMeasurementOutputPortPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
