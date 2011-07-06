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

public interface _ResourceOutputPortDel extends scm.eci.rt._OutputPortDel
{
    void send(Resource msg, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
