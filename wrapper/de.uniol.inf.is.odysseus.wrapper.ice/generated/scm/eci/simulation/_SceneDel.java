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

public interface _SceneDel extends scm.eci.rt._ComponentDel
{
    Resource[] getResources(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ResourceOutputPortPrx getResourceCreatedOutputPort(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ResourceOutputPortPrx getResourceDeletedOutputPort(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    String getFileName(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean isInitialized(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
