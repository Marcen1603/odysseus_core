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

public interface _TransformStateInputPortOperations extends scm.eci.rt._InputPortOperations
{
    TransformStateMessage getMessage(Ice.Current __current);

    TransformState getValue(Ice.Current __current);
}
