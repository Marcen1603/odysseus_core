// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.laser;

public interface _DebugRangeValueInputPortDel extends scm.eci.rt._InputPortDel
{
    DebugRangeValueMessage getMessage(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    DebugRangeValue getValue(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
