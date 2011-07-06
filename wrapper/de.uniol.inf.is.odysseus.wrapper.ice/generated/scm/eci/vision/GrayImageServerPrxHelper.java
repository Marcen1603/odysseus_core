// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.vision;

public final class GrayImageServerPrxHelper extends Ice.ObjectPrxHelperBase implements GrayImageServerPrx
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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

    public GrayImageOutputPortPrx
    getGrayImageOutputPort()
    {
        return getGrayImageOutputPort(null, false);
    }

    public GrayImageOutputPortPrx
    getGrayImageOutputPort(java.util.Map<String, String> __ctx)
    {
        return getGrayImageOutputPort(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private GrayImageOutputPortPrx
    getGrayImageOutputPort(java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                __checkTwowayOnly("getGrayImageOutputPort");
                __delBase = __getDelegate(false);
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
                return __del.getGrayImageOutputPort(__ctx);
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

    public ImageOutputPortPrx
    getImgOutputPort()
    {
        return getImgOutputPort(null, false);
    }

    public ImageOutputPortPrx
    getImgOutputPort(java.util.Map<String, String> __ctx)
    {
        return getImgOutputPort(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private ImageOutputPortPrx
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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

    public Point2i
    getResolution()
    {
        return getResolution(null, false);
    }

    public Point2i
    getResolution(java.util.Map<String, String> __ctx)
    {
        return getResolution(__ctx, true);
    }

    @SuppressWarnings("unchecked")
    private Point2i
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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
    setResolution(Point2i newValue)
    {
        return setResolution(newValue, null, false);
    }

    public boolean
    setResolution(Point2i newValue, java.util.Map<String, String> __ctx)
    {
        return setResolution(newValue, __ctx, true);
    }

    @SuppressWarnings("unchecked")
    private boolean
    setResolution(Point2i newValue, java.util.Map<String, String> __ctx, boolean __explicitCtx)
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
                _GrayImageServerDel __del = (_GrayImageServerDel)__delBase;
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

    public static GrayImageServerPrx
    checkedCast(Ice.ObjectPrx __obj)
    {
        GrayImageServerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GrayImageServerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::GrayImageServer"))
                {
                    GrayImageServerPrxHelper __h = new GrayImageServerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static GrayImageServerPrx
    checkedCast(Ice.ObjectPrx __obj, java.util.Map<String, String> __ctx)
    {
        GrayImageServerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GrayImageServerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                if(__obj.ice_isA("::scm::eci::vision::GrayImageServer", __ctx))
                {
                    GrayImageServerPrxHelper __h = new GrayImageServerPrxHelper();
                    __h.__copyFrom(__obj);
                    __d = __h;
                }
            }
        }
        return __d;
    }

    public static GrayImageServerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet)
    {
        GrayImageServerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::GrayImageServer"))
                {
                    GrayImageServerPrxHelper __h = new GrayImageServerPrxHelper();
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

    public static GrayImageServerPrx
    checkedCast(Ice.ObjectPrx __obj, String __facet, java.util.Map<String, String> __ctx)
    {
        GrayImageServerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            try
            {
                if(__bb.ice_isA("::scm::eci::vision::GrayImageServer", __ctx))
                {
                    GrayImageServerPrxHelper __h = new GrayImageServerPrxHelper();
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

    public static GrayImageServerPrx
    uncheckedCast(Ice.ObjectPrx __obj)
    {
        GrayImageServerPrx __d = null;
        if(__obj != null)
        {
            try
            {
                __d = (GrayImageServerPrx)__obj;
            }
            catch(ClassCastException ex)
            {
                GrayImageServerPrxHelper __h = new GrayImageServerPrxHelper();
                __h.__copyFrom(__obj);
                __d = __h;
            }
        }
        return __d;
    }

    public static GrayImageServerPrx
    uncheckedCast(Ice.ObjectPrx __obj, String __facet)
    {
        GrayImageServerPrx __d = null;
        if(__obj != null)
        {
            Ice.ObjectPrx __bb = __obj.ice_facet(__facet);
            GrayImageServerPrxHelper __h = new GrayImageServerPrxHelper();
            __h.__copyFrom(__bb);
            __d = __h;
        }
        return __d;
    }

    protected Ice._ObjectDelM
    __createDelegateM()
    {
        return new _GrayImageServerDelM();
    }

    protected Ice._ObjectDelD
    __createDelegateD()
    {
        return new _GrayImageServerDelD();
    }

    public static void
    __write(IceInternal.BasicStream __os, GrayImageServerPrx v)
    {
        __os.writeProxy(v);
    }

    public static GrayImageServerPrx
    __read(IceInternal.BasicStream __is)
    {
        Ice.ObjectPrx proxy = __is.readProxy();
        if(proxy != null)
        {
            GrayImageServerPrxHelper result = new GrayImageServerPrxHelper();
            result.__copyFrom(proxy);
            return result;
        }
        return null;
    }
}
