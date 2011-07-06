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

public interface _ScenarioOperationsNC extends _SceneOperationsNC
{
    SimAgentPrx[] getAgents();

    SimObjectPrx getObjectByUID(String uid);

    SimObjectPrx getObjectByName(String uid);

    scm.eci.StringOutputPortPrx getAgentCreatedOutputPort();

    scm.eci.StringOutputPortPrx getAgentDeletedOutputPort();
}
