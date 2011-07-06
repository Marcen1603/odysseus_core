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

public interface _SchedulerListenerOperationsNC
{
    void onJobInsert(JobDescription ctx);

    void onJobRemoved(JobPrx ctx);

    void onPreExecute(ExecutionContext ctx);

    void onPostExecute(ExecutionContext ctx);

    void onNextStep(int timestamp);
}
