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

public interface StrategiePrx extends Ice.ObjectPrx
{
    public String getName();
    public String getName(java.util.Map<String, String> __ctx);

    public String getDescription();
    public String getDescription(java.util.Map<String, String> __ctx);

    public boolean insert(JobDescription jobDesc);
    public boolean insert(JobDescription jobDesc, java.util.Map<String, String> __ctx);

    public boolean remove(JobPrx jobDesc);
    public boolean remove(JobPrx jobDesc, java.util.Map<String, String> __ctx);

    public boolean hasNextStep();
    public boolean hasNextStep(java.util.Map<String, String> __ctx);

    public int nextStep();
    public int nextStep(java.util.Map<String, String> __ctx);

    public boolean hasNextJob();
    public boolean hasNextJob(java.util.Map<String, String> __ctx);

    public JobDescription nextJob();
    public JobDescription nextJob(java.util.Map<String, String> __ctx);
}
