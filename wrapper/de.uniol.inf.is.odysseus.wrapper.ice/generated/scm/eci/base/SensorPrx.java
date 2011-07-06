// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.base;

public interface SensorPrx extends scm.eci.rt.ComponentPrx
{
    public int getFrequency();
    public int getFrequency(java.util.Map<String, String> __ctx);

    public boolean setFrequency(int newValue);
    public boolean setFrequency(int newValue, java.util.Map<String, String> __ctx);
}
