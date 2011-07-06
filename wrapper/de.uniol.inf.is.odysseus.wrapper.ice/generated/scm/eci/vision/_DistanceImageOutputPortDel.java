// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.vision;

public interface _DistanceImageOutputPortDel extends scm.eci.rt._OutputPortDel
{
    void send(DistanceImage msg, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
