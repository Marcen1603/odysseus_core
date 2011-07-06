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

public interface _SchedulerOperations
{
    String getName(Ice.Current __current);

    String getDescription(Ice.Current __current);

    StrategiePrx getStrategie(Ice.Current __current);

    boolean setStrategie(StrategiePrx newValue, Ice.Current __current);

    int getTimestamp(Ice.Current __current);

    JobProcessorPrx getDefaultProcessor(Ice.Current __current);

    boolean insert(JobDescription jobDesc, Ice.Current __current);

    boolean remove(JobPrx jobDesc, Ice.Current __current);

    boolean hasNextStep(Ice.Current __current);

    int nextStep(Ice.Current __current);

    boolean hasNextJob(Ice.Current __current);

    void executeNextJob(Ice.Current __current);

    void registerListener(SchedulerListenerPrx listener, Ice.Current __current);

    void removeListener(SchedulerListenerPrx listener, Ice.Current __current);
}
