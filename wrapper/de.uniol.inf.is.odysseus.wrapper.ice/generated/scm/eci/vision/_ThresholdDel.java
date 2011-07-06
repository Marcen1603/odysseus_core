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

public interface _ThresholdDel extends _AlgorithmDel
{
    int getThreshold(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setThreshold(int newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    ThresholdMode getMode(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setMode(ThresholdMode newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
