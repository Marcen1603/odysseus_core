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

public interface _TransformStateOperations
{
    scm.eci.base.Vector3 getPosition(Ice.Current __current);

    scm.eci.base.Vector3 getEuler(Ice.Current __current);

    scm.eci.base.Vector3 getScale(Ice.Current __current);
}
