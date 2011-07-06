// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.base;

public interface _SensorDel extends scm.eci.rt._ComponentDel
{
    int getFrequency(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean setFrequency(int newValue, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
