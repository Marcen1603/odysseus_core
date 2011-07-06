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

public interface _ThresholdOperations extends _AlgorithmOperations
{
    int getThreshold(Ice.Current __current);

    boolean setThreshold(int newValue, Ice.Current __current);

    ThresholdMode getMode(Ice.Current __current);

    boolean setMode(ThresholdMode newValue, Ice.Current __current);
}
