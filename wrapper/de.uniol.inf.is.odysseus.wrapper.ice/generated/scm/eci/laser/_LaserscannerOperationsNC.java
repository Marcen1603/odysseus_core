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

public interface _LaserscannerOperationsNC extends scm.eci.base._SensorOperationsNC
{
    scm.eci.base.Distance getMinRange();

    scm.eci.base.Distance getMaxRange();

    double getScanAngle();

    boolean setScanAngle(double newValue);

    double getAngleResolution();

    boolean setAngleResolution(double newValue);

    LaserMeasurementOutputPortPrx getScanOutputPort();
}
