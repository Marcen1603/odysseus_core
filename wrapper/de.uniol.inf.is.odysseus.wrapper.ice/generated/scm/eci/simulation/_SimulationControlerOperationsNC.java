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

public interface _SimulationControlerOperationsNC extends scm.eci.rt._ComponentOperationsNC
{
    scm.eci.scheduler2.SchedulerPrx getScheduler();

    ScenePrx[] getScenes();

    ScenarioPrx getActiveScenario();

    ScenePrx loadScene(scm.eci.base.File fileToLoad);

    SimAgentPrx[] instanceiateScene(ScenePrx sceneToInit);

    SimObjectPrx instanceiateResource(Resource res, SimAgentPrx parent);

    void renderFrame(SimCameraPrx camera);

    void doEvilRenderBypass(SimCameraPrx camera);

    void enableEvilRenderBypass(boolean enabled);

    boolean isEvilRenderBypassEnabled();

    void setDebugEnabled(boolean enabled);

    boolean isDebugEnabled();

    SimObjectPrx getObjectByName(String name);

    SimObjectPrx getObjectByUID(String uid);

    scm.eci.base.FileOutputPortPrx getSceneLoadedOutputPort();

    scm.eci.BoolOutputPortPrx getInitializedOutputPort();

    SimAgentPrx createNewAgentWithCamera();

    Resource createNewCamera(String name);
}
