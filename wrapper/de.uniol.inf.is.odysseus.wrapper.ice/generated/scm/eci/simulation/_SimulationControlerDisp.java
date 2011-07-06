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

public abstract class _SimulationControlerDisp extends Ice.ObjectImpl implements SimulationControler
{
    protected void
    ice_copyStateFrom(Ice.Object __obj)
        throws java.lang.CloneNotSupportedException
    {
        throw new java.lang.CloneNotSupportedException();
    }

    public static final String[] __ids =
    {
        "::Ice::Object",
        "::scm::eci::rt::Component",
        "::scm::eci::rt::SCMElement",
        "::scm::eci::rt::SCMRunnable",
        "::scm::eci::scheduler2::Job",
        "::scm::eci::simulation::SimulationControler"
    };

    public boolean
    ice_isA(String s)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public boolean
    ice_isA(String s, Ice.Current __current)
    {
        return java.util.Arrays.binarySearch(__ids, s) >= 0;
    }

    public String[]
    ice_ids()
    {
        return __ids;
    }

    public String[]
    ice_ids(Ice.Current __current)
    {
        return __ids;
    }

    public String
    ice_id()
    {
        return __ids[5];
    }

    public String
    ice_id(Ice.Current __current)
    {
        return __ids[5];
    }

    public static String
    ice_staticId()
    {
        return __ids[5];
    }

    public final scm.eci.rt.InputPortPrx
    getInputPort(String name)
    {
        return getInputPort(name, null);
    }

    public final scm.eci.rt.InputPortPrx
    getInputPortByUID(String uid)
    {
        return getInputPortByUID(uid, null);
    }

    public final scm.eci.rt.InputPortPrx[]
    getInputPorts()
    {
        return getInputPorts(null);
    }

    public final scm.eci.rt.OutputPortPrx
    getOutputPort(String name)
    {
        return getOutputPort(name, null);
    }

    public final scm.eci.rt.OutputPortPrx
    getOutputPortByUID(String uid)
    {
        return getOutputPortByUID(uid, null);
    }

    public final scm.eci.rt.OutputPortPrx[]
    getOutputPorts()
    {
        return getOutputPorts(null);
    }

    public final scm.eci.rt.PropertyPrx[]
    getProperties()
    {
        return getProperties(null);
    }

    public final scm.eci.rt.PropertyPrx
    getProperty(String name)
    {
        return getProperty(name, null);
    }

    public final scm.eci.rt.PropertyPrx
    getPropertyByUID(String uid)
    {
        return getPropertyByUID(uid, null);
    }

    public final String
    getDescription()
    {
        return getDescription(null);
    }

    public final String
    getName()
    {
        return getName(null);
    }

    public final String
    getUid()
    {
        return getUid(null);
    }

    public final void
    run()
    {
        run(null);
    }

    public final boolean
    canExecute()
    {
        return canExecute(null);
    }

    public final boolean
    execute(scm.eci.scheduler2.ExecutionContext ctx)
    {
        return execute(ctx, null);
    }

    public final scm.eci.scheduler2.JobProcessorPrx
    getActiveProcessor()
    {
        return getActiveProcessor(null);
    }

    public final String
    getJobDescription()
    {
        return getJobDescription(null);
    }

    public final String
    getJobName()
    {
        return getJobName(null);
    }

    public final boolean
    setActiveProcessor(scm.eci.scheduler2.JobProcessorPrx newValue)
    {
        return setActiveProcessor(newValue, null);
    }

    public final SimAgentPrx
    createNewAgentWithCamera()
    {
        return createNewAgentWithCamera(null);
    }

    public final Resource
    createNewCamera(String name)
    {
        return createNewCamera(name, null);
    }

    public final void
    doEvilRenderBypass(SimCameraPrx camera)
    {
        doEvilRenderBypass(camera, null);
    }

    public final void
    enableEvilRenderBypass(boolean enabled)
    {
        enableEvilRenderBypass(enabled, null);
    }

    public final ScenarioPrx
    getActiveScenario()
    {
        return getActiveScenario(null);
    }

    public final scm.eci.BoolOutputPortPrx
    getInitializedOutputPort()
    {
        return getInitializedOutputPort(null);
    }

    public final SimObjectPrx
    getObjectByName(String name)
    {
        return getObjectByName(name, null);
    }

    public final SimObjectPrx
    getObjectByUID(String uid)
    {
        return getObjectByUID(uid, null);
    }

    public final scm.eci.base.FileOutputPortPrx
    getSceneLoadedOutputPort()
    {
        return getSceneLoadedOutputPort(null);
    }

    public final ScenePrx[]
    getScenes()
    {
        return getScenes(null);
    }

    public final scm.eci.scheduler2.SchedulerPrx
    getScheduler()
    {
        return getScheduler(null);
    }

    public final SimObjectPrx
    instanceiateResource(Resource res, SimAgentPrx parent)
    {
        return instanceiateResource(res, parent, null);
    }

    public final SimAgentPrx[]
    instanceiateScene(ScenePrx sceneToInit)
    {
        return instanceiateScene(sceneToInit, null);
    }

    public final boolean
    isDebugEnabled()
    {
        return isDebugEnabled(null);
    }

    public final boolean
    isEvilRenderBypassEnabled()
    {
        return isEvilRenderBypassEnabled(null);
    }

    public final ScenePrx
    loadScene(scm.eci.base.File fileToLoad)
    {
        return loadScene(fileToLoad, null);
    }

    public final void
    renderFrame(SimCameraPrx camera)
    {
        renderFrame(camera, null);
    }

    public final void
    setDebugEnabled(boolean enabled)
    {
        setDebugEnabled(enabled, null);
    }

    public static Ice.DispatchStatus
    ___getScheduler(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        scm.eci.scheduler2.SchedulerPrx __ret = __obj.getScheduler(__current);
        scm.eci.scheduler2.SchedulerPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getScenes(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        ScenePrx[] __ret = __obj.getScenes(__current);
        SceneSeqHelper.write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getActiveScenario(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        ScenarioPrx __ret = __obj.getActiveScenario(__current);
        ScenarioPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___loadScene(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        scm.eci.base.FileHolder fileToLoad = new scm.eci.base.FileHolder();
        __is.readObject(fileToLoad.getPatcher());
        __is.readPendingObjects();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        ScenePrx __ret = __obj.loadScene(fileToLoad.value, __current);
        ScenePrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___instanceiateScene(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        ScenePrx sceneToInit;
        sceneToInit = ScenePrxHelper.__read(__is);
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimAgentPrx[] __ret = __obj.instanceiateScene(sceneToInit, __current);
        SimAgentSeqHelper.write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___instanceiateResource(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        ResourceHolder res = new ResourceHolder();
        __is.readObject(res.getPatcher());
        SimAgentPrx parent;
        parent = SimAgentPrxHelper.__read(__is);
        __is.readPendingObjects();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimObjectPrx __ret = __obj.instanceiateResource(res.value, parent, __current);
        SimObjectPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___renderFrame(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        SimCameraPrx camera;
        camera = SimCameraPrxHelper.__read(__is);
        __is.endReadEncaps();
        __obj.renderFrame(camera, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___doEvilRenderBypass(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        SimCameraPrx camera;
        camera = SimCameraPrxHelper.__read(__is);
        __is.endReadEncaps();
        __obj.doEvilRenderBypass(camera, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___enableEvilRenderBypass(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        boolean enabled;
        enabled = __is.readBool();
        __is.endReadEncaps();
        __obj.enableEvilRenderBypass(enabled, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___isEvilRenderBypassEnabled(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.isEvilRenderBypassEnabled(__current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___setDebugEnabled(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        boolean enabled;
        enabled = __is.readBool();
        __is.endReadEncaps();
        __obj.setDebugEnabled(enabled, __current);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___isDebugEnabled(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        boolean __ret = __obj.isDebugEnabled(__current);
        __os.writeBool(__ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getObjectByName(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String name;
        name = __is.readString();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimObjectPrx __ret = __obj.getObjectByName(name, __current);
        SimObjectPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getObjectByUID(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String uid;
        uid = __is.readString();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimObjectPrx __ret = __obj.getObjectByUID(uid, __current);
        SimObjectPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getSceneLoadedOutputPort(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        scm.eci.base.FileOutputPortPrx __ret = __obj.getSceneLoadedOutputPort(__current);
        scm.eci.base.FileOutputPortPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___getInitializedOutputPort(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        scm.eci.BoolOutputPortPrx __ret = __obj.getInitializedOutputPort(__current);
        scm.eci.BoolOutputPortPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewAgentWithCamera(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        __inS.is().skipEmptyEncaps();
        IceInternal.BasicStream __os = __inS.os();
        SimAgentPrx __ret = __obj.createNewAgentWithCamera(__current);
        SimAgentPrxHelper.__write(__os, __ret);
        return Ice.DispatchStatus.DispatchOK;
    }

    public static Ice.DispatchStatus
    ___createNewCamera(SimulationControler __obj, IceInternal.Incoming __inS, Ice.Current __current)
    {
        __checkMode(Ice.OperationMode.Normal, __current.mode);
        IceInternal.BasicStream __is = __inS.is();
        __is.startReadEncaps();
        String name;
        name = __is.readString();
        __is.endReadEncaps();
        IceInternal.BasicStream __os = __inS.os();
        Resource __ret = __obj.createNewCamera(name, __current);
        __os.writeObject(__ret);
        __os.writePendingObjects();
        return Ice.DispatchStatus.DispatchOK;
    }

    private final static String[] __all =
    {
        "canExecute",
        "createNewAgentWithCamera",
        "createNewCamera",
        "doEvilRenderBypass",
        "enableEvilRenderBypass",
        "execute",
        "getActiveProcessor",
        "getActiveScenario",
        "getDescription",
        "getInitializedOutputPort",
        "getInputPort",
        "getInputPortByUID",
        "getInputPorts",
        "getJobDescription",
        "getJobName",
        "getName",
        "getObjectByName",
        "getObjectByUID",
        "getOutputPort",
        "getOutputPortByUID",
        "getOutputPorts",
        "getProperties",
        "getProperty",
        "getPropertyByUID",
        "getSceneLoadedOutputPort",
        "getScenes",
        "getScheduler",
        "getUid",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "instanceiateResource",
        "instanceiateScene",
        "isDebugEnabled",
        "isEvilRenderBypassEnabled",
        "loadScene",
        "renderFrame",
        "run",
        "setActiveProcessor",
        "setDebugEnabled"
    };

    public Ice.DispatchStatus
    __dispatch(IceInternal.Incoming in, Ice.Current __current)
    {
        int pos = java.util.Arrays.binarySearch(__all, __current.operation);
        if(pos < 0)
        {
            throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return scm.eci.scheduler2._JobDisp.___canExecute(this, in, __current);
            }
            case 1:
            {
                return ___createNewAgentWithCamera(this, in, __current);
            }
            case 2:
            {
                return ___createNewCamera(this, in, __current);
            }
            case 3:
            {
                return ___doEvilRenderBypass(this, in, __current);
            }
            case 4:
            {
                return ___enableEvilRenderBypass(this, in, __current);
            }
            case 5:
            {
                return scm.eci.scheduler2._JobDisp.___execute(this, in, __current);
            }
            case 6:
            {
                return scm.eci.scheduler2._JobDisp.___getActiveProcessor(this, in, __current);
            }
            case 7:
            {
                return ___getActiveScenario(this, in, __current);
            }
            case 8:
            {
                return scm.eci.rt._SCMElementDisp.___getDescription(this, in, __current);
            }
            case 9:
            {
                return ___getInitializedOutputPort(this, in, __current);
            }
            case 10:
            {
                return scm.eci.rt._ComponentDisp.___getInputPort(this, in, __current);
            }
            case 11:
            {
                return scm.eci.rt._ComponentDisp.___getInputPortByUID(this, in, __current);
            }
            case 12:
            {
                return scm.eci.rt._ComponentDisp.___getInputPorts(this, in, __current);
            }
            case 13:
            {
                return scm.eci.scheduler2._JobDisp.___getJobDescription(this, in, __current);
            }
            case 14:
            {
                return scm.eci.scheduler2._JobDisp.___getJobName(this, in, __current);
            }
            case 15:
            {
                return scm.eci.rt._SCMElementDisp.___getName(this, in, __current);
            }
            case 16:
            {
                return ___getObjectByName(this, in, __current);
            }
            case 17:
            {
                return ___getObjectByUID(this, in, __current);
            }
            case 18:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPort(this, in, __current);
            }
            case 19:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPortByUID(this, in, __current);
            }
            case 20:
            {
                return scm.eci.rt._ComponentDisp.___getOutputPorts(this, in, __current);
            }
            case 21:
            {
                return scm.eci.rt._ComponentDisp.___getProperties(this, in, __current);
            }
            case 22:
            {
                return scm.eci.rt._ComponentDisp.___getProperty(this, in, __current);
            }
            case 23:
            {
                return scm.eci.rt._ComponentDisp.___getPropertyByUID(this, in, __current);
            }
            case 24:
            {
                return ___getSceneLoadedOutputPort(this, in, __current);
            }
            case 25:
            {
                return ___getScenes(this, in, __current);
            }
            case 26:
            {
                return ___getScheduler(this, in, __current);
            }
            case 27:
            {
                return scm.eci.rt._SCMElementDisp.___getUid(this, in, __current);
            }
            case 28:
            {
                return ___ice_id(this, in, __current);
            }
            case 29:
            {
                return ___ice_ids(this, in, __current);
            }
            case 30:
            {
                return ___ice_isA(this, in, __current);
            }
            case 31:
            {
                return ___ice_ping(this, in, __current);
            }
            case 32:
            {
                return ___instanceiateResource(this, in, __current);
            }
            case 33:
            {
                return ___instanceiateScene(this, in, __current);
            }
            case 34:
            {
                return ___isDebugEnabled(this, in, __current);
            }
            case 35:
            {
                return ___isEvilRenderBypassEnabled(this, in, __current);
            }
            case 36:
            {
                return ___loadScene(this, in, __current);
            }
            case 37:
            {
                return ___renderFrame(this, in, __current);
            }
            case 38:
            {
                return scm.eci.rt._SCMRunnableDisp.___run(this, in, __current);
            }
            case 39:
            {
                return scm.eci.scheduler2._JobDisp.___setActiveProcessor(this, in, __current);
            }
            case 40:
            {
                return ___setDebugEnabled(this, in, __current);
            }
        }

        assert(false);
        throw new Ice.OperationNotExistException(__current.id, __current.facet, __current.operation);
    }

    public void
    __write(IceInternal.BasicStream __os)
    {
        __os.writeTypeId(ice_staticId());
        __os.startWriteSlice();
        __os.endWriteSlice();
        super.__write(__os);
    }

    public void
    __read(IceInternal.BasicStream __is, boolean __rid)
    {
        if(__rid)
        {
            __is.readTypeId();
        }
        __is.startReadSlice();
        __is.endReadSlice();
        super.__read(__is, true);
    }

    public void
    __write(Ice.OutputStream __outS)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::simulation::SimulationControler was not generated with stream support";
        throw ex;
    }

    public void
    __read(Ice.InputStream __inS, boolean __rid)
    {
        Ice.MarshalException ex = new Ice.MarshalException();
        ex.reason = "type scm::eci::simulation::SimulationControler was not generated with stream support";
        throw ex;
    }
}
