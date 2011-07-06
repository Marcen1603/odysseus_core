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

public interface _SimBehaviorOperationsNC extends _SimObjectOperationsNC,
                                                  scm.eci.scheduler2._JobOperationsNC
{
    SimAgentPrx getAgent();

    void setAgent(SimAgentPrx agent);

    boolean isDebug();

    void onCreation();

    void onDestruction();

    void scheduleOnce(int relativeMilliseconds);

    void scheduleInterval(int relativStartMilli, int intervalMilli);

    boolean isRepeatableScheduled();

    void disableSchedule();
}
