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

public interface ResourceOutputPortPrx extends scm.eci.rt.OutputPortPrx
{
    public void send(Resource msg);
    public void send(Resource msg, java.util.Map<String, String> __ctx);
}
