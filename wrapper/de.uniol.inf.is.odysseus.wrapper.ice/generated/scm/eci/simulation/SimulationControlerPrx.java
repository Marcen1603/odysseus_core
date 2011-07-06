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

public interface SimulationControlerPrx extends scm.eci.rt.ComponentPrx
{
    public scm.eci.scheduler2.SchedulerPrx getScheduler();
    public scm.eci.scheduler2.SchedulerPrx getScheduler(java.util.Map<String, String> __ctx);

    public ScenePrx[] getScenes();
    public ScenePrx[] getScenes(java.util.Map<String, String> __ctx);

    public ScenarioPrx getActiveScenario();
    public ScenarioPrx getActiveScenario(java.util.Map<String, String> __ctx);

    public ScenePrx loadScene(scm.eci.base.File fileToLoad);
    public ScenePrx loadScene(scm.eci.base.File fileToLoad, java.util.Map<String, String> __ctx);

    public SimAgentPrx[] instanceiateScene(ScenePrx sceneToInit);
    public SimAgentPrx[] instanceiateScene(ScenePrx sceneToInit, java.util.Map<String, String> __ctx);

    public SimObjectPrx instanceiateResource(Resource res, SimAgentPrx parent);
    public SimObjectPrx instanceiateResource(Resource res, SimAgentPrx parent, java.util.Map<String, String> __ctx);

    public void renderFrame(SimCameraPrx camera);
    public void renderFrame(SimCameraPrx camera, java.util.Map<String, String> __ctx);

    public void doEvilRenderBypass(SimCameraPrx camera);
    public void doEvilRenderBypass(SimCameraPrx camera, java.util.Map<String, String> __ctx);

    public void enableEvilRenderBypass(boolean enabled);
    public void enableEvilRenderBypass(boolean enabled, java.util.Map<String, String> __ctx);

    public boolean isEvilRenderBypassEnabled();
    public boolean isEvilRenderBypassEnabled(java.util.Map<String, String> __ctx);

    public void setDebugEnabled(boolean enabled);
    public void setDebugEnabled(boolean enabled, java.util.Map<String, String> __ctx);

    public boolean isDebugEnabled();
    public boolean isDebugEnabled(java.util.Map<String, String> __ctx);

    public SimObjectPrx getObjectByName(String name);
    public SimObjectPrx getObjectByName(String name, java.util.Map<String, String> __ctx);

    public SimObjectPrx getObjectByUID(String uid);
    public SimObjectPrx getObjectByUID(String uid, java.util.Map<String, String> __ctx);

    public scm.eci.base.FileOutputPortPrx getSceneLoadedOutputPort();
    public scm.eci.base.FileOutputPortPrx getSceneLoadedOutputPort(java.util.Map<String, String> __ctx);

    public scm.eci.BoolOutputPortPrx getInitializedOutputPort();
    public scm.eci.BoolOutputPortPrx getInitializedOutputPort(java.util.Map<String, String> __ctx);

    public SimAgentPrx createNewAgentWithCamera();
    public SimAgentPrx createNewAgentWithCamera(java.util.Map<String, String> __ctx);

    public Resource createNewCamera(String name);
    public Resource createNewCamera(String name, java.util.Map<String, String> __ctx);
}
