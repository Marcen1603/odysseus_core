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

public interface ScenarioPrx extends ScenePrx
{
    public SimAgentPrx[] getAgents();
    public SimAgentPrx[] getAgents(java.util.Map<String, String> __ctx);

    public SimObjectPrx getObjectByUID(String uid);
    public SimObjectPrx getObjectByUID(String uid, java.util.Map<String, String> __ctx);

    public SimObjectPrx getObjectByName(String uid);
    public SimObjectPrx getObjectByName(String uid, java.util.Map<String, String> __ctx);

    public scm.eci.StringOutputPortPrx getAgentCreatedOutputPort();
    public scm.eci.StringOutputPortPrx getAgentCreatedOutputPort(java.util.Map<String, String> __ctx);

    public scm.eci.StringOutputPortPrx getAgentDeletedOutputPort();
    public scm.eci.StringOutputPortPrx getAgentDeletedOutputPort(java.util.Map<String, String> __ctx);
}
