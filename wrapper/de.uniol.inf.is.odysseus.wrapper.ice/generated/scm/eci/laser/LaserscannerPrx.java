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

public interface LaserscannerPrx extends scm.eci.base.SensorPrx
{
    public scm.eci.base.Distance getMinRange();
    public scm.eci.base.Distance getMinRange(java.util.Map<String, String> __ctx);

    public scm.eci.base.Distance getMaxRange();
    public scm.eci.base.Distance getMaxRange(java.util.Map<String, String> __ctx);

    public double getScanAngle();
    public double getScanAngle(java.util.Map<String, String> __ctx);

    public boolean setScanAngle(double newValue);
    public boolean setScanAngle(double newValue, java.util.Map<String, String> __ctx);

    public double getAngleResolution();
    public double getAngleResolution(java.util.Map<String, String> __ctx);

    public boolean setAngleResolution(double newValue);
    public boolean setAngleResolution(double newValue, java.util.Map<String, String> __ctx);

    public LaserMeasurementOutputPortPrx getScanOutputPort();
    public LaserMeasurementOutputPortPrx getScanOutputPort(java.util.Map<String, String> __ctx);
}
