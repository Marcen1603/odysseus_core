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

public interface _StrategieDel extends Ice._ObjectDel
{
    String getName(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    String getDescription(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean insert(JobDescription jobDesc, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean remove(JobPrx jobDesc, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean hasNextStep(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    int nextStep(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean hasNextJob(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    JobDescription nextJob(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
