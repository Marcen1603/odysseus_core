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

public interface Scheduler2FacadePrx extends Ice.ObjectPrx
{
    public StrategiePrx createNewStrategie();
    public StrategiePrx createNewStrategie(java.util.Map<String, String> __ctx);

    public SchedulerPrx createNewScheduler();
    public SchedulerPrx createNewScheduler(java.util.Map<String, String> __ctx);

    public Ice.ObjectPrx getObjectByUID(String uid);
    public Ice.ObjectPrx getObjectByUID(String uid, java.util.Map<String, String> __ctx);

    public void releaseObjectByUID(String uid);
    public void releaseObjectByUID(String uid, java.util.Map<String, String> __ctx);
}
