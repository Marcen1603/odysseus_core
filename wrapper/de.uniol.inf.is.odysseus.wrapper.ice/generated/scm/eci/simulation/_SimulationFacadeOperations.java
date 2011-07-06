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

public interface _SimulationFacadeOperations
{
    SimAgentPrx createNewSimAgent(Ice.Current __current);

    SimCameraPrx createNewSimCamera(Ice.Current __current);

    SimLaserscannerPrx createNewSimLaserscanner(Ice.Current __current);

    ScenePrx createNewScene(Ice.Current __current);

    ScenarioPrx createNewScenario(Ice.Current __current);

    SimulationControlerPrx createNewSimulationControler(Ice.Current __current);

    Ice.ObjectPrx getObjectByUID(String uid, Ice.Current __current);

    void releaseObjectByUID(String uid, Ice.Current __current);
}
