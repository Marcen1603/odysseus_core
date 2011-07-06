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

public final class SimAgentPrxHelper extends Ice.ObjectPrxHelperBase implements SimAgentPrx
{
    public scm.eci.rt.InputPortPrx
    getInputPort(String name)
    {
        return getInputPort(name, null, false);
    }

    public scm.eci.rt.InputPortPrx
    getInputPort(String name, java.util.Map<String, String> __ctx)
    {
        return getInputPort(name, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.InputPortPrx
    getInputPort(String name, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getInputPort");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getInputPort(name, __ctx);
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

    public scm.eci.rt.InputPortPrx
    getInputPortByUID(String uid)
    {
        return getInputPortByUID(uid, null, false);
    }

    public scm.eci.rt.InputPortPrx
    getInputPortByUID(String uid, java.util.Map<String, String> __ctx)
    {
        return getInputPortByUID(uid, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.InputPortPrx
    getInputPortByUID(String uid, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getInputPortByUID");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getInputPortByUID(uid, __ctx);
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
    getInputPorts()
    {
        return getInputPorts(null, false);
    }

    public scm.eci.rt.InputPortPrx[]
    getInputPorts(java.util.Map<String, String> __ctx)
    {
        return getInputPorts(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.InputPortPrx[]
    getInputPorts(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getInputPorts");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getInputPorts(__ctx);
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
    getOutputPort(String name)
    {
        return getOutputPort(name, null, false);
    }

    public scm.eci.rt.OutputPortPrx
    getOutputPort(String name, java.util.Map<String, String> __ctx)
    {
        return getOutputPort(name, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.OutputPortPrx
    getOutputPort(String name, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getOutputPort");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getOutputPort(name, __ctx);
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
    getOutputPortByUID(String uid)
    {
        return getOutputPortByUID(uid, null, false);
    }

    public scm.eci.rt.OutputPortPrx
    getOutputPortByUID(String uid, java.util.Map<String, String> __ctx)
    {
        return getOutputPortByUID(uid, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.OutputPortPrx
    getOutputPortByUID(String uid, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getOutputPortByUID");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getOutputPortByUID(uid, __ctx);
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

    public scm.eci.rt.OutputPortPrx[]
    getOutputPorts()
    {
        return getOutputPorts(null, false);
    }

    public scm.eci.rt.OutputPortPrx[]
    getOutputPorts(java.util.Map<String, String> __ctx)
    {
        return getOutputPorts(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.OutputPortPrx[]
    getOutputPorts(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getOutputPorts");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getOutputPorts(__ctx);
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

    public scm.eci.rt.PropertyPrx[]
    getProperties()
    {
        return getProperties(null, false);
    }

    public scm.eci.rt.PropertyPrx[]
    getProperties(java.util.Map<String, String> __ctx)
    {
        return getProperties(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.PropertyPrx[]
    getProperties(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getProperties");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getProperties(__ctx);
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

    public scm.eci.rt.PropertyPrx
    getProperty(String name)
    {
        return getProperty(name, null, false);
    }

    public scm.eci.rt.PropertyPrx
    getProperty(String name, java.util.Map<String, String> __ctx)
    {
        return getProperty(name, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.PropertyPrx
    getProperty(String name, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getProperty");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getProperty(name, __ctx);
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

    public scm.eci.rt.PropertyPrx
    getPropertyByUID(String uid)
    {
        return getPropertyByUID(uid, null, false);
    }

    public scm.eci.rt.PropertyPrx
    getPropertyByUID(String uid, java.util.Map<String, String> __ctx)
    {
        return getPropertyByUID(uid, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.rt.PropertyPrx
    getPropertyByUID(String uid, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getPropertyByUID");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getPropertyByUID(uid, __ctx);
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
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

    public void
    run()
    {
        run(null, false);
    }

    public void
    run(java.util.Map<String, String> __ctx)
    {
        run(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    run(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                __del.run(__ctx);
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
    canExecute()
    {
        return canExecute(null, false);
    }

    public boolean
    canExecute(java.util.Map<String, String> __ctx)
    {
        return canExecute(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    canExecute(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("canExecute");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.canExecute(__ctx);
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
    execute(scm.eci.scheduler2.ExecutionContext ctx)
    {
        return execute(ctx, null, false);
    }

    public boolean
    execute(scm.eci.scheduler2.ExecutionContext ctx, java.util.Map<String, String> __ctx)
    {
        return execute(ctx, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    execute(scm.eci.scheduler2.ExecutionContext ctx, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("execute");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.execute(ctx, __ctx);
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

    public scm.eci.scheduler2.JobProcessorPrx
    getActiveProcessor()
    {
        return getActiveProcessor(null, false);
    }

    public scm.eci.scheduler2.JobProcessorPrx
    getActiveProcessor(java.util.Map<String, String> __ctx)
    {
        return getActiveProcessor(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.scheduler2.JobProcessorPrx
    getActiveProcessor(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getActiveProcessor");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getActiveProcessor(__ctx);
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
    getJobDescription()
    {
        return getJobDescription(null, false);
    }

    public String
    getJobDescription(java.util.Map<String, String> __ctx)
    {
        return getJobDescription(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getJobDescription(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getJobDescription");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getJobDescription(__ctx);
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
    getJobName()
    {
        return getJobName(null, false);
    }

    public String
    getJobName(java.util.Map<String, String> __ctx)
    {
        return getJobName(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getJobName(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getJobName");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getJobName(__ctx);
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
    setActiveProcessor(scm.eci.scheduler2.JobProcessorPrx newValue)
    {
        return setActiveProcessor(newValue, null, false);
    }

    public boolean
    setActiveProcessor(scm.eci.scheduler2.JobProcessorPrx newValue, java.util.Map<String, String> __ctx)
    {
        return setActiveProcessor(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setActiveProcessor(scm.eci.scheduler2.JobProcessorPrx newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setActiveProcessor");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.setActiveProcessor(newValue, __ctx);
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

    public SimBehaviorPrx
    getBehaviorByName(String uid)
    {
        return getBehaviorByName(uid, null, false);
    }

    public SimBehaviorPrx
    getBehaviorByName(String uid, java.util.Map<String, String> __ctx)
    {
        return getBehaviorByName(uid, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private SimBehaviorPrx
    getBehaviorByName(String uid, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getBehaviorByName");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getBehaviorByName(uid, __ctx);
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

    public SimBehaviorPrx
    getBehaviorByUID(String uid)
    {
        return getBehaviorByUID(uid, null, false);
    }

    public SimBehaviorPrx
    getBehaviorByUID(String uid, java.util.Map<String, String> __ctx)
    {
        return getBehaviorByUID(uid, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private SimBehaviorPrx
    getBehaviorByUID(String uid, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getBehaviorByUID");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getBehaviorByUID(uid, __ctx);
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

    public SimBehaviorPrx[]
    getBehaviors()
    {
        return getBehaviors(null, false);
    }

    public SimBehaviorPrx[]
    getBehaviors(java.util.Map<String, String> __ctx)
    {
        return getBehaviors(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private SimBehaviorPrx[]
    getBehaviors(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getBehaviors");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getBehaviors(__ctx);
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

    public scm.eci.base.Polygon3
    getBoundingPolygon()
    {
        return getBoundingPolygon(null, false);
    }

    public scm.eci.base.Polygon3
    getBoundingPolygon(java.util.Map<String, String> __ctx)
    {
        return getBoundingPolygon(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.base.Polygon3
    getBoundingPolygon(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getBoundingPolygon");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getBoundingPolygon(__ctx);
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

    public BoundingVolume
    getBoundingVolume()
    {
        return getBoundingVolume(null, false);
    }

    public BoundingVolume
    getBoundingVolume(java.util.Map<String, String> __ctx)
    {
        return getBoundingVolume(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private BoundingVolume
    getBoundingVolume(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getBoundingVolume");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getBoundingVolume(__ctx);
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
    registerBehavior(SimBehaviorPrx behavior)
    {
        registerBehavior(behavior, null, false);
    }

    public void
    registerBehavior(SimBehaviorPrx behavior, java.util.Map<String, String> __ctx)
    {
        registerBehavior(behavior, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    registerBehavior(SimBehaviorPrx behavior, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                __del.registerBehavior(behavior, __ctx);
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
    removeBehavior(SimBehaviorPrx behavior)
    {
        removeBehavior(behavior, null, false);
    }

    public void
    removeBehavior(SimBehaviorPrx behavior, java.util.Map<String, String> __ctx)
    {
        removeBehavior(behavior, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    removeBehavior(SimBehaviorPrx behavior, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                __del.removeBehavior(behavior, __ctx);
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
    setBehaviors(SimBehaviorPrx[] newValue)
    {
        return setBehaviors(newValue, null, false);
    }

    public boolean
    setBehaviors(SimBehaviorPrx[] newValue, java.util.Map<String, String> __ctx)
    {
        return setBehaviors(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setBehaviors(SimBehaviorPrx[] newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setBehaviors");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.setBehaviors(newValue, __ctx);
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
    setLocalTransform(TransformState transform)
    {
        setLocalTransform(transform, null, false);
    }

    public void
    setLocalTransform(TransformState transform, java.util.Map<String, String> __ctx)
    {
        setLocalTransform(transform, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    setLocalTransform(TransformState transform, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                __del.setLocalTransform(transform, __ctx);
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
    setPosition(double x, double y, double z)
    {
        setPosition(x, y, z, null, false);
    }

    public void
    setPosition(double x, double y, double z, java.util.Map<String, String> __ctx)
    {
        setPosition(x, y, z, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    setPosition(double x, double y, double z, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                __del.setPosition(x, y, z, __ctx);
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
    setRotationEuler(double x, double y, double z)
    {
        setRotationEuler(x, y, z, null, false);
    }

    public void
    setRotationEuler(double x, double y, double z, java.util.Map<String, String> __ctx)
    {
        setRotationEuler(x, y, z, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    setRotationEuler(double x, double y, double z, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                __del.setRotationEuler(x, y, z, __ctx);
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
    setRotationMatrix(scm.eci.base.Matrix3x3 matrix)
    {
        setRotationMatrix(matrix, null, false);
    }

    public void
    setRotationMatrix(scm.eci.base.Matrix3x3 matrix, java.util.Map<String, String> __ctx)
    {
        setRotationMatrix(matrix, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    setRotationMatrix(scm.eci.base.Matrix3x3 matrix, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                __del.setRotationMatrix(matrix, __ctx);
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
    setScale(double x, double y, double z)
    {
        setScale(x, y, z, null, false);
    }

    public void
    setScale(double x, double y, double z, java.util.Map<String, String> __ctx)
    {
        setScale(x, y, z, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    setScale(double x, double y, double z, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                __del.setScale(x, y, z, __ctx);
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
    setWorldTransform(TransformState transform)
    {
        setWorldTransform(transform, null, false);
    }

    public void
    setWorldTransform(TransformState transform, java.util.Map<String, String> __ctx)
    {
        setWorldTransform(transform, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    setWorldTransform(TransformState transform, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                __del.setWorldTransform(transform, __ctx);
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

    public TransformState
    getLocalTransform()
    {
        return getLocalTransform(null, false);
    }

    public TransformState
    getLocalTransform(java.util.Map<String, String> __ctx)
    {
        return getLocalTransform(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private TransformState
    getLocalTransform(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getLocalTransform");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getLocalTransform(__ctx);
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
    getResourceUID()
    {
        return getResourceUID(null, false);
    }

    public String
    getResourceUID(java.util.Map<String, String> __ctx)
    {
        return getResourceUID(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getResourceUID(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getResourceUID");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getResourceUID(__ctx);
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

    public TransformStateOutputPortPrx
    getTransformOutputPort()
    {
        return getTransformOutputPort(null, false);
    }

    public TransformStateOutputPortPrx
    getTransformOutputPort(java.util.Map<String, String> __ctx)
    {
        return getTransformOutputPort(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private TransformStateOutputPortPrx
    getTransformOutputPort(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getTransformOutputPort");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getTransformOutputPort(__ctx);
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

    public TransformState
    getWorldTransform()
    {
        return getWorldTransform(null, false);
    }

    public TransformState
    getWorldTransform(java.util.Map<String, String> __ctx)
    {
        return getWorldTransform(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private TransformState
    getWorldTransform(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getWorldTransform");
                __delBase = __getDelegate(false);
                _SimAgentDel __del = (_SimAgentDel)__delBase;
                return __del.getWorldTransform(__ctx);
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

    public static SimAgentPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        SimAgentPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SimAgentPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::SimAgent"))
                {
                    SimAgentPrxHelper __h = new SimAgentPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SimAgentPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        SimAgentPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SimAgentPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::SimAgent", __ctx))
                {
                    SimAgentPrxHelper __h = new SimAgentPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SimAgentPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SimAgentPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::SimAgent"))
                {
                    SimAgentPrxHelper __h = new SimAgentPrxHelper();
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

    public static SimAgentPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        SimAgentPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::SimAgent", __ctx))
                {
                    SimAgentPrxHelper __h = new SimAgentPrxHelper();
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

    public static SimAgentPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        SimAgentPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SimAgentPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                SimAgentPrxHelper __h = new SimAgentPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static SimAgentPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SimAgentPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            SimAgentPrxHelper __h = new SimAgentPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _SimAgentDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _SimAgentDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, SimAgentPrx v)
    {
        __os.writeProxy(v);
    }

    public static SimAgentPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            SimAgentPrxHelper result = new SimAgentPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
