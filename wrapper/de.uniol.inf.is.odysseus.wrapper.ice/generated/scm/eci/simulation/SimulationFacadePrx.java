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

public interface SimulationFacadePrx extends Ice.ObjectPrx
{
    public SimAgentPrx createNewSimAgent();
    public SimAgentPrx createNewSimAgent(java.util.Map<String, String> __ctx);

    public SimCameraPrx createNewSimCamera();
    public SimCameraPrx createNewSimCamera(java.util.Map<String, String> __ctx);

    public SimLaserscannerPrx createNewSimLaserscanner();
    public SimLaserscannerPrx createNewSimLaserscanner(java.util.Map<String, String> __ctx);

    public ScenePrx createNewScene();
    public ScenePrx createNewScene(java.util.Map<String, String> __ctx);

    public ScenarioPrx createNewScenario();
    public ScenarioPrx createNewScenario(java.util.Map<String, String> __ctx);

    public SimulationControlerPrx createNewSimulationControler();
    public SimulationControlerPrx createNewSimulationControler(java.util.Map<String, String> __ctx);

    public Ice.ObjectPrx getObjectByUID(String uid);
    public Ice.ObjectPrx getObjectByUID(String uid, java.util.Map<String, String> __ctx);

    public void releaseObjectByUID(String uid);
    public void releaseObjectByUID(String uid, java.util.Map<String, String> __ctx);
}
