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

public interface JobPrx extends Ice.ObjectPrx
{
    public JobProcessorPrx getActiveProcessor();
    public JobProcessorPrx getActiveProcessor(java.util.Map<String, String> __ctx);

    public boolean setActiveProcessor(JobProcessorPrx newValue);
    public boolean setActiveProcessor(JobProcessorPrx newValue, java.util.Map<String, String> __ctx);

    public boolean canExecute();
    public boolean canExecute(java.util.Map<String, String> __ctx);

    public boolean execute(ExecutionContext ctx);
    public boolean execute(ExecutionContext ctx, java.util.Map<String, String> __ctx);

    public String getJobName();
    public String getJobName(java.util.Map<String, String> __ctx);

    public String getJobDescription();
    public String getJobDescription(java.util.Map<String, String> __ctx);
}
