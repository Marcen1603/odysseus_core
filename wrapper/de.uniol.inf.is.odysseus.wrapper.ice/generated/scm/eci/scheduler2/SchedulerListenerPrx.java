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

public interface SchedulerListenerPrx extends Ice.ObjectPrx
{
    public void onJobInsert(JobDescription ctx);
    public void onJobInsert(JobDescription ctx, java.util.Map<String, String> __ctx);

    public void onJobRemoved(JobPrx ctx);
    public void onJobRemoved(JobPrx ctx, java.util.Map<String, String> __ctx);

    public void onPreExecute(ExecutionContext ctx);
    public void onPreExecute(ExecutionContext ctx, java.util.Map<String, String> __ctx);

    public void onPostExecute(ExecutionContext ctx);
    public void onPostExecute(ExecutionContext ctx, java.util.Map<String, String> __ctx);

    public void onNextStep(int timestamp);
    public void onNextStep(int timestamp, java.util.Map<String, String> __ctx);
}
