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

public interface _SimulationControlerDel extends scm.eci.rt._ComponentDel
{
    scm.eci.scheduler2.SchedulerPrx getScheduler(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ScenePrx[] getScenes(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ScenarioPrx getActiveScenario(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ScenePrx loadScene(scm.eci.base.File fileToLoad, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    SimAgentPrx[] instanceiateScene(ScenePrx sceneToInit, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    SimObjectPrx instanceiateResource(Resource res, SimAgentPrx parent, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void renderFrame(SimCameraPrx camera, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void doEvilRenderBypass(SimCameraPrx camera, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void enableEvilRenderBypass(boolean enabled, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean isEvilRenderBypassEnabled(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void setDebugEnabled(boolean enabled, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean isDebugEnabled(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    SimObjectPrx getObjectByName(String name, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    SimObjectPrx getObjectByUID(String uid, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    scm.eci.base.FileOutputPortPrx getSceneLoadedOutputPort(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    scm.eci.BoolOutputPortPrx getInitializedOutputPort(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    SimAgentPrx createNewAgentWithCamera(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    Resource createNewCamera(String name, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
