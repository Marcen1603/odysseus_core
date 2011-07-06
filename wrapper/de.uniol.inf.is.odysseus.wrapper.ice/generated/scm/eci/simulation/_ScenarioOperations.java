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

public interface _ScenarioOperations extends _SceneOperations
{
    SimAgentPrx[] getAgents(Ice.Current __current);

    SimObjectPrx getObjectByUID(String uid, Ice.Current __current);

    SimObjectPrx getObjectByName(String uid, Ice.Current __current);

    scm.eci.StringOutputPortPrx getAgentCreatedOutputPort(Ice.Current __current);

    scm.eci.StringOutputPortPrx getAgentDeletedOutputPort(Ice.Current __current);
}
