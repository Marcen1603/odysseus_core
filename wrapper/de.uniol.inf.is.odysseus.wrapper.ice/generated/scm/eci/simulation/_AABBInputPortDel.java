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

public interface _AABBInputPortDel extends scm.eci.rt._InputPortDel
{
    AABBMessage getMessage(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    AABB getValue(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
