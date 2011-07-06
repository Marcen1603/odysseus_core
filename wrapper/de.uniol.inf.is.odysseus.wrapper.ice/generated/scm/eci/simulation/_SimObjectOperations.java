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

public interface _SimObjectOperations extends scm.eci.rt._ComponentOperations
{
    TransformState getLocalTransform(Ice.Current __current);

    TransformState getWorldTransform(Ice.Current __current);

    TransformStateOutputPortPrx getTransformOutputPort(Ice.Current __current);

    String getResourceUID(Ice.Current __current);
}
