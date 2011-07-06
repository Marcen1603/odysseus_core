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

public final class ScenePrxHelper extends Ice.ObjectPrxHelperBase implements ScenePrx
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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
                _SceneDel __del = (_SceneDel)__delBase;
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

    public String
    getFileName()
    {
        return getFileName(null, false);
    }

    public String
    getFileName(java.util.Map<String, String> __ctx)
    {
        return getFileName(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private String
    getFileName(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getFileName");
                __delBase = __getDelegate(false);
                _SceneDel __del = (_SceneDel)__delBase;
                return __del.getFileName(__ctx);
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

    public ResourceOutputPortPrx
    getResourceCreatedOutputPort()
    {
        return getResourceCreatedOutputPort(null, false);
    }

    public ResourceOutputPortPrx
    getResourceCreatedOutputPort(java.util.Map<String, String> __ctx)
    {
        return getResourceCreatedOutputPort(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private ResourceOutputPortPrx
    getResourceCreatedOutputPort(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getResourceCreatedOutputPort");
                __delBase = __getDelegate(false);
                _SceneDel __del = (_SceneDel)__delBase;
                return __del.getResourceCreatedOutputPort(__ctx);
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

    public ResourceOutputPortPrx
    getResourceDeletedOutputPort()
    {
        return getResourceDeletedOutputPort(null, false);
    }

    public ResourceOutputPortPrx
    getResourceDeletedOutputPort(java.util.Map<String, String> __ctx)
    {
        return getResourceDeletedOutputPort(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private ResourceOutputPortPrx
    getResourceDeletedOutputPort(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getResourceDeletedOutputPort");
                __delBase = __getDelegate(false);
                _SceneDel __del = (_SceneDel)__delBase;
                return __del.getResourceDeletedOutputPort(__ctx);
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

    public Resource[]
    getResources()
    {
        return getResources(null, false);
    }

    public Resource[]
    getResources(java.util.Map<String, String> __ctx)
    {
        return getResources(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private Resource[]
    getResources(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getResources");
                __delBase = __getDelegate(false);
                _SceneDel __del = (_SceneDel)__delBase;
                return __del.getResources(__ctx);
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
    isInitialized()
    {
        return isInitialized(null, false);
    }

    public boolean
    isInitialized(java.util.Map<String, String> __ctx)
    {
        return isInitialized(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    isInitialized(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("isInitialized");
                __delBase = __getDelegate(false);
                _SceneDel __del = (_SceneDel)__delBase;
                return __del.isInitialized(__ctx);
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

    public static ScenePrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        ScenePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ScenePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::Scene"))
                {
                    ScenePrxHelper __h = new ScenePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ScenePrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        ScenePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ScenePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::simulation::Scene", __ctx))
                {
                    ScenePrxHelper __h = new ScenePrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static ScenePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ScenePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::Scene"))
                {
                    ScenePrxHelper __h = new ScenePrxHelper();
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

    public static ScenePrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        ScenePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::simulation::Scene", __ctx))
                {
                    ScenePrxHelper __h = new ScenePrxHelper();
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

    public static ScenePrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        ScenePrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (ScenePrx)__obj;
            }
            catch(ClassCastException ex)
            {
                ScenePrxHelper __h = new ScenePrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static ScenePrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        ScenePrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            ScenePrxHelper __h = new ScenePrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _SceneDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _SceneDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, ScenePrx v)
    {
        __os.writeProxy(v);
    }

    public static ScenePrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            ScenePrxHelper result = new ScenePrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
