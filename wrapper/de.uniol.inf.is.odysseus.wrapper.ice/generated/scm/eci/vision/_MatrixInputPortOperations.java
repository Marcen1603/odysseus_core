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

public interface _MatrixInputPortOperations extends scm.eci.rt._InputPortOperations
{
    MatrixMessage getMessage(Ice.Current __current);

    Matrix getValue(Ice.Current __current);
}
