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

public interface _SimBehaviorDel extends _SimObjectDel,
                                         scm.eci.scheduler2._JobDel
{
    SimAgentPrx getAgent(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void setAgent(SimAgentPrx agent, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean isDebug(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void onCreation(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void onDestruction(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void scheduleOnce(int relativeMilliseconds, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void scheduleInterval(int relativStartMilli, int intervalMilli, java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    boolean isRepeatableScheduled(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;

    void disableSchedule(java.util.Map<String, String> __ctx)
        throws IceInternal.LocalExceptionWrapper;
}
