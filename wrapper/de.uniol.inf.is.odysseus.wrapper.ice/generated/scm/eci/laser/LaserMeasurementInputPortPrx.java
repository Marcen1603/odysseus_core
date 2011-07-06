// **********************************************************************
//
// Copyright (c) 2003-2009 ZeroC, Inc. All rights reserved.
//
// This copy of Ice is licensed to you under the terms described in the
// ICE_LICENSE file included in this distribution.
//
// **********************************************************************

// Ice version 3.3.1

package scm.eci.laser;

public interface LaserMeasurementInputPortPrx extends scm.eci.rt.InputPortPrx
{
    public LaserMeasurementMessage getMessage();
    public LaserMeasurementMessage getMessage(java.util.Map<String, String> __ctx);

    public LaserMeasurement getValue();
    public LaserMeasurement getValue(java.util.Map<String, String> __ctx);
}
