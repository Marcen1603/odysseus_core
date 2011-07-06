// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.scheduler2;

public interface _ProcessMonitorDel extends Ice._ObjectDel
{
    void setStatus(JobStatus status, String msg, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void setProgress(int progress, String msg, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
