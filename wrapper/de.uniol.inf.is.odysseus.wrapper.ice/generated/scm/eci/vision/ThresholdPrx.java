// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.vision;

public interface ThresholdPrx extends AlgorithmPrx
{
    public int getThreshold();
    public int getThreshold(java.util.Map<String, String> __ctx);

    public boolean setThreshold(int newValue);
    public boolean setThreshold(int newValue, java.util.Map<String, String> __ctx);

    public ThresholdMode getMode();
    public ThresholdMode getMode(java.util.Map<String, String> __ctx);

    public boolean setMode(ThresholdMode newValue);
    public boolean setMode(ThresholdMode newValue, java.util.Map<String, String> __ctx);
}
