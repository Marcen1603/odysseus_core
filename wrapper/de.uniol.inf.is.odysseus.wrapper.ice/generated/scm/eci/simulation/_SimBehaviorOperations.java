// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.simulation;

public interface _SimBehaviorOperations extends _SimObjectOperations,
                                                scm.eci.scheduler2._JobOperations
{
    SimAgentPrx getAgent(Ice.Current __current);

    void setAgent(SimAgentPrx agent, Ice.Current __current);

    boolean isDebug(Ice.Current __current);

    void onCreation(Ice.Current __current);

    void onDestruction(Ice.Current __current);

    void scheduleOnce(int relativeMilliseconds, Ice.Current __current);

    void scheduleInterval(int relativStartMilli, int intervalMilli, Ice.Current __current);

    boolean isRepeatableScheduled(Ice.Current __current);

    void disableSchedule(Ice.Current __current);
}
