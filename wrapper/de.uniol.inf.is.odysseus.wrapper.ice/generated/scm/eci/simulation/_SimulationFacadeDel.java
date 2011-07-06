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

public interface _SimulationFacadeDel extends Ice._ObjectDel
{
    SimAgentPrx createNewSimAgent(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    SimCameraPrx createNewSimCamera(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    SimLaserscannerPrx createNewSimLaserscanner(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ScenePrx createNewScene(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ScenarioPrx createNewScenario(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    SimulationControlerPrx createNewSimulationControler(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    Ice.ObjectPrx getObjectByUID(String uid, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void releaseObjectByUID(String uid, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
