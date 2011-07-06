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

public interface ScenePrx extends scm.eci.rt.ComponentPrx
{
    public Resource[] getResources();
    public Resource[] getResources(java.util.Map<String, String> __ctx);

    public ResourceOutputPortPrx getResourceCreatedOutputPort();
    public ResourceOutputPortPrx getResourceCreatedOutputPort(java.util.Map<String, String> __ctx);

    public ResourceOutputPortPrx getResourceDeletedOutputPort();
    public ResourceOutputPortPrx getResourceDeletedOutputPort(java.util.Map<String, String> __ctx);

    public String getFileName();
    public String getFileName(java.util.Map<String, String> __ctx);

    public boolean isInitialized();
    public boolean isInitialized(java.util.Map<String, String> __ctx);
}
