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

public interface _JobOperations
{
    JobProcessorPrx getActiveProcessor(Ice.Current __current);

    boolean setActiveProcessor(JobProcessorPrx newValue, Ice.Current __current);

    boolean canExecute(Ice.Current __current);

    boolean execute(ExecutionContext ctx, Ice.Current __current);

    String getJobName(Ice.Current __current);

    String getJobDescription(Ice.Current __current);
}
