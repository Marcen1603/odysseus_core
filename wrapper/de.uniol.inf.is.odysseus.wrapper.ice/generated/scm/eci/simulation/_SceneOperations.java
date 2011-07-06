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

public interface _SceneOperations extends scm.eci.rt._ComponentOperations
{
    Resource[] getResources(Ice.Current __current);

    ResourceOutputPortPrx getResourceCreatedOutputPort(Ice.Current __current);

    ResourceOutputPortPrx getResourceDeletedOutputPort(Ice.Current __current);

    String getFileName(Ice.Current __current);

    boolean isInitialized(Ice.Current __current);
}
