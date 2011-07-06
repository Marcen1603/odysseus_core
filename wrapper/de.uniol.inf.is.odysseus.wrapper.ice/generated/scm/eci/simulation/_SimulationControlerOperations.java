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

public interface _SimulationControlerOperations extends scm.eci.rt._ComponentOperations
{
    scm.eci.scheduler2.SchedulerPrx getScheduler(Ice.Current __current);

    ScenePrx[] getScenes(Ice.Current __current);

    ScenarioPrx getActiveScenario(Ice.Current __current);

    ScenePrx loadScene(scm.eci.base.File fileToLoad, Ice.Current __current);

    SimAgentPrx[] instanceiateScene(ScenePrx sceneToInit, Ice.Current __current);

    SimObjectPrx instanceiateResource(Resource res, SimAgentPrx parent, Ice.Current __current);

    void renderFrame(SimCameraPrx camera, Ice.Current __current);

    void doEvilRenderBypass(SimCameraPrx camera, Ice.Current __current);

    void enableEvilRenderBypass(boolean enabled, Ice.Current __current);

    boolean isEvilRenderBypassEnabled(Ice.Current __current);

    void setDebugEnabled(boolean enabled, Ice.Current __current);

    boolean isDebugEnabled(Ice.Current __current);

    SimObjectPrx getObjectByName(String name, Ice.Current __current);

    SimObjectPrx getObjectByUID(String uid, Ice.Current __current);

    scm.eci.base.FileOutputPortPrx getSceneLoadedOutputPort(Ice.Current __current);

    scm.eci.BoolOutputPortPrx getInitializedOutputPort(Ice.Current __current);

    SimAgentPrx createNewAgentWithCamera(Ice.Current __current);

    Resource createNewCamera(String name, Ice.Current __current);
}
