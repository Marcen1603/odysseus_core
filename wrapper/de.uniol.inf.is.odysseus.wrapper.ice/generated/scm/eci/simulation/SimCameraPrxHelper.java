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

public final class SimCameraPrxHelper extends Ice.ObjectPrxHelperBase implements SimCameraPrx
{
    public int
    getFrequency()
    {
        return getFrequency(null, false);
    }

    public int
    getFrequency(java.util.Map<String, String> __ctx)
    {
        return getFrequency(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private int
    getFrequency(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getFrequency");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getFrequency(__ctx);
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
    setFrequency(int newValue)
    {
        return setFrequency(newValue, null, false);
    }

    public boolean
    setFrequency(int newValue, java.util.Map<String, String> __ctx)
    {
        return setFrequency(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setFrequency(int newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setFrequency");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.setFrequency(newValue, __ctx);
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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

    public void
    disableSchedule()
    {
        disableSchedule(null, false);
    }

    public void
    disableSchedule(java.util.Map<String, String> __ctx)
    {
        disableSchedule(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    disableSchedule(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                __del.disableSchedule(__ctx);
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

    public SimAgentPrx
    getAgent()
    {
        return getAgent(null, false);
    }

    public SimAgentPrx
    getAgent(java.util.Map<String, String> __ctx)
    {
        return getAgent(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private SimAgentPrx
    getAgent(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getAgent");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getAgent(__ctx);
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
    isDebug()
    {
        return isDebug(null, false);
    }

    public boolean
    isDebug(java.util.Map<String, String> __ctx)
    {
        return isDebug(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    isDebug(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("isDebug");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.isDebug(__ctx);
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
    isRepeatableScheduled()
    {
        return isRepeatableScheduled(null, false);
    }

    public boolean
    isRepeatableScheduled(java.util.Map<String, String> __ctx)
    {
        return isRepeatableScheduled(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    isRepeatableScheduled(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("isRepeatableScheduled");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.isRepeatableScheduled(__ctx);
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
    onCreation()
    {
        onCreation(null, false);
    }

    public void
    onCreation(java.util.Map<String, String> __ctx)
    {
        onCreation(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    onCreation(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                __del.onCreation(__ctx);
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
    onDestruction()
    {
        onDestruction(null, false);
    }

    public void
    onDestruction(java.util.Map<String, String> __ctx)
    {
        onDestruction(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    onDestruction(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                __del.onDestruction(__ctx);
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
    scheduleInterval(int relativStartMilli, int intervalMilli)
    {
        scheduleInterval(relativStartMilli, intervalMilli, null, false);
    }

    public void
    scheduleInterval(int relativStartMilli, int intervalMilli, java.util.Map<String, String> __ctx)
    {
        scheduleInterval(relativStartMilli, intervalMilli, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    scheduleInterval(int relativStartMilli, int intervalMilli, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                __del.scheduleInterval(relativStartMilli, intervalMilli, __ctx);
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
    scheduleOnce(int relativeMilliseconds)
    {
        scheduleOnce(relativeMilliseconds, null, false);
    }

    public void
    scheduleOnce(int relativeMilliseconds, java.util.Map<String, String> __ctx)
    {
        scheduleOnce(relativeMilliseconds, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    scheduleOnce(int relativeMilliseconds, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                __del.scheduleOnce(relativeMilliseconds, __ctx);
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
    setAgent(SimAgentPrx agent)
    {
        setAgent(agent, null, false);
    }

    public void
    setAgent(SimAgentPrx agent, java.util.Map<String, String> __ctx)
    {
        setAgent(agent, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    setAgent(SimAgentPrx agent, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                __del.setAgent(agent, __ctx);
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

    public double
    getFar()
    {
        return getFar(null, false);
    }

    public double
    getFar(java.util.Map<String, String> __ctx)
    {
        return getFar(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private double
    getFar(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getFar");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getFar(__ctx);
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

    public double
    getFocalLength()
    {
        return getFocalLength(null, false);
    }

    public double
    getFocalLength(java.util.Map<String, String> __ctx)
    {
        return getFocalLength(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private double
    getFocalLength(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getFocalLength");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getFocalLength(__ctx);
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

    public double
    getFov()
    {
        return getFov(null, false);
    }

    public double
    getFov(java.util.Map<String, String> __ctx)
    {
        return getFov(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private double
    getFov(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getFov");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getFov(__ctx);
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

    public double
    getNear()
    {
        return getNear(null, false);
    }

    public double
    getNear(java.util.Map<String, String> __ctx)
    {
        return getNear(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private double
    getNear(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getNear");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getNear(__ctx);
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

    public double
    getOrthoScale()
    {
        return getOrthoScale(null, false);
    }

    public double
    getOrthoScale(java.util.Map<String, String> __ctx)
    {
        return getOrthoScale(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private double
    getOrthoScale(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getOrthoScale");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getOrthoScale(__ctx);
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
    getOrthographic()
    {
        return getOrthographic(null, false);
    }

    public boolean
    getOrthographic(java.util.Map<String, String> __ctx)
    {
        return getOrthographic(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    getOrthographic(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getOrthographic");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getOrthographic(__ctx);
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
    makeCurrent()
    {
        makeCurrent(null, false);
    }

    public void
    makeCurrent(java.util.Map<String, String> __ctx)
    {
        makeCurrent(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    makeCurrent(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                __del.makeCurrent(__ctx);
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
    move(double x, double y, double z, boolean moveAgent)
    {
        move(x, y, z, moveAgent, null, false);
    }

    public void
    move(double x, double y, double z, boolean moveAgent, java.util.Map<String, String> __ctx)
    {
        move(x, y, z, moveAgent, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    move(double x, double y, double z, boolean moveAgent, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                __del.move(x, y, z, moveAgent, __ctx);
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
    rotateEuler(double x, double y, double z, boolean rotateAgent)
    {
        rotateEuler(x, y, z, rotateAgent, null, false);
    }

    public void
    rotateEuler(double x, double y, double z, boolean rotateAgent, java.util.Map<String, String> __ctx)
    {
        rotateEuler(x, y, z, rotateAgent, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    rotateEuler(double x, double y, double z, boolean rotateAgent, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                __del.rotateEuler(x, y, z, rotateAgent, __ctx);
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
    setFar(double newValue)
    {
        return setFar(newValue, null, false);
    }

    public boolean
    setFar(double newValue, java.util.Map<String, String> __ctx)
    {
        return setFar(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setFar(double newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setFar");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.setFar(newValue, __ctx);
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
    setFocalLength(double newValue)
    {
        return setFocalLength(newValue, null, false);
    }

    public boolean
    setFocalLength(double newValue, java.util.Map<String, String> __ctx)
    {
        return setFocalLength(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setFocalLength(double newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setFocalLength");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.setFocalLength(newValue, __ctx);
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
    setFov(double newValue)
    {
        return setFov(newValue, null, false);
    }

    public boolean
    setFov(double newValue, java.util.Map<String, String> __ctx)
    {
        return setFov(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setFov(double newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setFov");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.setFov(newValue, __ctx);
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
    setNear(double newValue)
    {
        return setNear(newValue, null, false);
    }

    public boolean
    setNear(double newValue, java.util.Map<String, String> __ctx)
    {
        return setNear(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setNear(double newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setNear");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.setNear(newValue, __ctx);
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
    setOrthoScale(double newValue)
    {
        return setOrthoScale(newValue, null, false);
    }

    public boolean
    setOrthoScale(double newValue, java.util.Map<String, String> __ctx)
    {
        return setOrthoScale(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setOrthoScale(double newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setOrthoScale");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.setOrthoScale(newValue, __ctx);
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
    setOrthographic(boolean newValue)
    {
        return setOrthographic(newValue, null, false);
    }

    public boolean
    setOrthographic(boolean newValue, java.util.Map<String, String> __ctx)
    {
        return setOrthographic(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setOrthographic(boolean newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setOrthographic");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.setOrthographic(newValue, __ctx);
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
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

    public void
    capture(double simTime)
    {
        capture(simTime, null, false);
    }

    public void
    capture(double simTime, java.util.Map<String, String> __ctx)
    {
        capture(simTime, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private void
    capture(double simTime, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                __del.capture(simTime, __ctx);
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

    public scm.eci.vision.ImageOutputPortPrx
    getImgOutputPort()
    {
        return getImgOutputPort(null, false);
    }

    public scm.eci.vision.ImageOutputPortPrx
    getImgOutputPort(java.util.Map<String, String> __ctx)
    {
        return getImgOutputPort(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.vision.ImageOutputPortPrx
    getImgOutputPort(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getImgOutputPort");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getImgOutputPort(__ctx);
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

    public scm.eci.vision.Point2i
    getResolution()
    {
        return getResolution(null, false);
    }

    public scm.eci.vision.Point2i
    getResolution(java.util.Map<String, String> __ctx)
    {
        return getResolution(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.vision.Point2i
    getResolution(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getResolution");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getResolution(__ctx);
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
    setResolution(scm.eci.vision.Point2i newValue)
    {
        return setResolution(newValue, null, false);
    }

    public boolean
    setResolution(scm.eci.vision.Point2i newValue, java.util.Map<String, String> __ctx)
    {
        return setResolution(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setResolution(scm.eci.vision.Point2i newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("setResolution");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.setResolution(newValue, __ctx);
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

    public scm.eci.vision.RGBImageOutputPortPrx
    getColorImageOutputPort()
    {
        return getColorImageOutputPort(null, false);
    }

    public scm.eci.vision.RGBImageOutputPortPrx
    getColorImageOutputPort(java.util.Map<String, String> __ctx)
    {
        return getColorImageOutputPort(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private scm.eci.vision.RGBImageOutputPortPrx
    getColorImageOutputPort(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getColorImageOutputPort");
                __delBase = __getDelegate(false);
                _SimCameraDel __del = (_SimCameraDel)__delBase;
                return __del.getColorImageOutputPort(__ctx);
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

    public static SimCameraPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        SimCameraPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SimCameraPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::SimCamera"))
                {
                    SimCameraPrxHelper __h = new SimCameraPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SimCameraPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        SimCameraPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SimCameraPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::SimCamera", __ctx))
                {
                    SimCameraPrxHelper __h = new SimCameraPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static SimCameraPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SimCameraPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::SimCamera"))
                {
                    SimCameraPrxHelper __h = new SimCameraPrxHelper();
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

    public static SimCameraPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        SimCameraPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::SimCamera", __ctx))
                {
                    SimCameraPrxHelper __h = new SimCameraPrxHelper();
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

    public static SimCameraPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        SimCameraPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (SimCameraPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                SimCameraPrxHelper __h = new SimCameraPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static SimCameraPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        SimCameraPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            SimCameraPrxHelper __h = new SimCameraPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _SimCameraDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _SimCameraDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, SimCameraPrx v)
    {
        __os.writeProxy(v);
    }

    public static SimCameraPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            SimCameraPrxHelper result = new SimCameraPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
