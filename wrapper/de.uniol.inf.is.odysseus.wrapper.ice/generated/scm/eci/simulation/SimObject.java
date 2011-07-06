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

public interface SimObject extends Ice.Object,
                                   _SimObjectOperations, _SimObjectOperationsNC,
                                   scm.eci.rt.Component
{
}
