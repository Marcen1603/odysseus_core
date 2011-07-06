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

public interface SimBehaviorPrx extends SimObjectPrx,
                                        scm.eci.scheduler2.JobPrx
{
    public SimAgentPrx getAgent();
    public SimAgentPrx getAgent(java.util.Map<String, String> __ctx);

    public void setAgent(SimAgentPrx agent);
    public void setAgent(SimAgentPrx agent, java.util.Map<String, String> __ctx);

    public boolean isDebug();
    public boolean isDebug(java.util.Map<String, String> __ctx);

    public void onCreation();
    public void onCreation(java.util.Map<String, String> __ctx);

    public void onDestruction();
    public void onDestruction(java.util.Map<String, String> __ctx);

    public void scheduleOnce(int relativeMilliseconds);
    public void scheduleOnce(int relativeMilliseconds, java.util.Map<String, String> __ctx);

    public void scheduleInterval(int relativStartMilli, int intervalMilli);
    public void scheduleInterval(int relativStartMilli, int intervalMilli, java.util.Map<String, String> __ctx);

    public boolean isRepeatableScheduled();
    public boolean isRepeatableScheduled(java.util.Map<String, String> __ctx);

    public void disableSchedule();
    public void disableSchedule(java.util.Map<String, String> __ctx);
}
