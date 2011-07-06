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

public interface _SchedulerListenerDel extends Ice._ObjectDel
{
    void onJobInsert(JobDescription ctx, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void onJobRemoved(JobPrx ctx, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void onPreExecute(ExecutionContext ctx, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void onPostExecute(ExecutionContext ctx, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void onNextStep(int timestamp, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
