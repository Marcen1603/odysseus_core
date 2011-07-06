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

public interface _SchedulerOperationsNC
{
    String getName();

    String getDescription();

    StrategiePrx getStrategie();

    boolean setStrategie(StrategiePrx newValue);

    int getTimestamp();

    JobProcessorPrx getDefaultProcessor();

    boolean insert(JobDescription jobDesc);

    boolean remove(JobPrx jobDesc);

    boolean hasNextStep();

    int nextStep();

    boolean hasNextJob();

    void executeNextJob();

    void registerListener(SchedulerListenerPrx listener);

    void removeListener(SchedulerListenerPrx listener);
}
